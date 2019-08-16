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

import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WaitTask extends DefaultTask {

    private Property<String> podName;

    public WaitTask() {
        podName = getProject().getObjects().property(String.class);
    }

    @Input
    public Property<String> getPodName() {
        return podName;
    }

    @TaskAction
    public void taskAction() throws IOException, InterruptedException {
        Process waitProcess = new ProcessBuilder("kubectl", "wait", "--for=condition=Ready", "pod/" + podName.get())
                .start();

        Logger logger = getProject().getLogger();
        int process = waitProcess.waitFor();
        if (process != 0) {
            String e = IOUtils.toString(waitProcess.getErrorStream(), StandardCharsets.UTF_8);
            logger.error("Failed to wait for Pod: " + e);
            throw new GradleException(e);
        }

        String s = IOUtils.toString(waitProcess.getInputStream(), StandardCharsets.UTF_8);
        logger.info(s);
    }
}
