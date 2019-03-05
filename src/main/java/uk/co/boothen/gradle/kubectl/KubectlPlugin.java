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

import uk.co.boothen.gradle.kubectl.extension.KubectlPluginExtension;
import uk.co.boothen.gradle.kubectl.task.ForwardPortTask;
import uk.co.boothen.gradle.kubectl.task.StartTask;
import uk.co.boothen.gradle.kubectl.task.StopForwardPortTask;
import uk.co.boothen.gradle.kubectl.task.StopPodTask;
import uk.co.boothen.gradle.kubectl.task.StopServiceTask;
import uk.co.boothen.gradle.kubectl.task.WaitTask;

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

            StartTask startTask = project.getTasks().create("startPod", StartTask.class);
            startTask.getFile().set(project.file(extension.getFile()));

            WaitTask waitTask = project.getTasks().create("waitingForPod", WaitTask.class);
            waitTask.getPodName().set(extension.getPodName());
            waitTask.mustRunAfter(startTask);

            ForwardPortTask forwardPortTask = project.getTasks().create("forwardPort", ForwardPortTask.class);
            forwardPortTask.getPodName().set(extension.getPodName());
            forwardPortTask.getPortForward().set(extension.getPortForward());
            forwardPortTask.mustRunAfter(waitTask);

            extension.getRequiredBy().dependsOn(startTask, waitTask, forwardPortTask);

            StopPodTask stopPodTask = project.getTasks().create("stopPod", StopPodTask.class);
            stopPodTask.getPodName().set(extension.getPodName());

            StopServiceTask stopServiceTask = project.getTasks().create("stopService", StopServiceTask.class);
            stopServiceTask.getServiceName().set(extension.getPodName());

            stopServiceTask.mustRunAfter(stopPodTask);

            StopForwardPortTask stopForwardPortTask = project.getTasks().create("stopForwardPort", StopForwardPortTask.class);
            stopForwardPortTask.setForwardPortTask(forwardPortTask);

            extension.getRequiredBy().finalizedBy(stopForwardPortTask, stopPodTask, stopServiceTask);
        });
    }
}
