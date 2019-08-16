/*
 * Copyright 2019 Boothen Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.boothen.gradle.kubectl;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import uk.co.boothen.gradle.kubectl.extension.KubectlPluginExtension;
import uk.co.boothen.gradle.kubectl.extension.Pod;
import uk.co.boothen.gradle.kubectl.extension.PortForward;
import uk.co.boothen.gradle.kubectl.task.ForwardPortTask;
import uk.co.boothen.gradle.kubectl.task.StartTask;
import uk.co.boothen.gradle.kubectl.task.StopForwardPortTask;
import uk.co.boothen.gradle.kubectl.task.StopPodTask;
import uk.co.boothen.gradle.kubectl.task.StopServiceTask;
import uk.co.boothen.gradle.kubectl.task.WaitTask;

import java.util.ArrayList;
import java.util.List;

public class KubectlPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getExtensions().add("kubectl", KubectlPluginExtension.class);

        // Need to check kubectl installed and version 1.12 or above.

        project.afterEvaluate(action -> {
            KubectlPluginExtension extension = action.getExtensions().getByType(KubectlPluginExtension.class);
            if (extension.getRequiredBy() == null) {
                System.out.println("No requiredBy defined. ");
                return;
            }

            List<Task> dependsOnTaskList = new ArrayList<>();
            if (extension.isForceStopBeforeStart()) {
                for (Pod pod : extension.getPod()) {
                    StopPodTask stopPodTask = project.getTasks().create("stopOrphanedPod-" + pod.getPodName(), StopPodTask.class);
                    stopPodTask.getPodName().set(pod.getPodName());
                    dependsOnTaskList.add(stopPodTask);
                }

                for (Pod pod : extension.getPod()) {
                    StopServiceTask stopServiceTask = project.getTasks().create("stopOrphanedService-" + pod.getPodName(), StopServiceTask.class);
                    stopServiceTask.getServiceName().set(pod.getPodName());
                    dependsOnTaskList.add(stopServiceTask);
                }
            }

            StartTask startTask = project.getTasks().create("startPod", StartTask.class);
            startTask.getFile().set(project.file(extension.getFile()));
            startTask.mustRunAfter(dependsOnTaskList.toArray());
            dependsOnTaskList.add(startTask);

            Task lastWaitTask = null;
            for (Pod pod : extension.getPod()) {
                WaitTask waitTask2 = project.getTasks().create("waitingForPod-" + pod.getPodName(), WaitTask.class);
                waitTask2.getPodName().set(pod.getPodName());
                waitTask2.getWaitForTimeout().set(extension.getWaitForTimeout());
                waitTask2.mustRunAfter(startTask);
                dependsOnTaskList.add(waitTask2);
                lastWaitTask = waitTask2;
            }

            List<ForwardPortTask> portForwardTaskList = new ArrayList<>();
            for (PortForward portForward : extension.getPortForward()) {
                ForwardPortTask forwardPortTask2 = project.getTasks().create("forwardPort-" + portForward.getService() + "-" + portForward.getPort(), ForwardPortTask.class);
                forwardPortTask2.getPodName().set(portForward.getService());
                forwardPortTask2.getPort().set(portForward.getPort());
                forwardPortTask2.getTargetPort().set(portForward.getTargetPort());
                forwardPortTask2.mustRunAfter(lastWaitTask);
                dependsOnTaskList.add(forwardPortTask2);
                portForwardTaskList.add(forwardPortTask2);
            }

            extension.getRequiredBy().dependsOn(dependsOnTaskList.toArray());

            List<Task> finalizedByTaskList = new ArrayList<>();
            for (Pod pod : extension.getPod()) {
                StopPodTask stopPodTask = project.getTasks().create("stopPod-" + pod.getPodName(), StopPodTask.class);
                stopPodTask.getPodName().set(pod.getPodName());
                finalizedByTaskList.add(stopPodTask);
            }

            for (Pod pod : extension.getPod()) {
                StopServiceTask stopServiceTask = project.getTasks().create("stopService-" + pod.getPodName(), StopServiceTask.class);
                stopServiceTask.getServiceName().set(pod.getPodName());
                finalizedByTaskList.add(stopServiceTask);
            }

            for (ForwardPortTask forwardPortTask : portForwardTaskList) {
                StopForwardPortTask stopForwardPortTask = project.getTasks().create("stopForwardPort-" + forwardPortTask.taskName(), StopForwardPortTask.class);
                stopForwardPortTask.setForwardPortTask(forwardPortTask);
                finalizedByTaskList.add(stopForwardPortTask);
            }

            extension.getRequiredBy().finalizedBy(finalizedByTaskList);
        });
    }
}
