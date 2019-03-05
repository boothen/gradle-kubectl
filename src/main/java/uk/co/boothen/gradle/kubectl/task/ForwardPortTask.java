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

package uk.co.boothen.gradle.kubectl.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class ForwardPortTask extends DefaultTask {

    private Property<String> podName;
    private Property<Integer> portForward;
    private Process process;

    public ForwardPortTask() {
        podName = getProject().getObjects().property(String.class);
        portForward = getProject().getObjects().property(Integer.class);
    }

    @Input
    public Property<String> getPodName() {
        return podName;
    }

    @Input
    public Property<Integer> getPortForward() {
        return portForward;
    }

    public Process process() {
        return process;
    }

    @TaskAction
    public void taskAction() throws IOException, InterruptedException {

        if (!portForward.isPresent()) {
            return;
        }

        process = new ProcessBuilder("kubectl", "port-forward", podName.get(), String.format("%d:%d", portForward.get(), portForward.get()))
                .start();
    }

}
