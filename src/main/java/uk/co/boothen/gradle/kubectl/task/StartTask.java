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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class StartTask extends DefaultTask {

    private Property<File> file;

    public StartTask() {
        file = getProject().getObjects().property(File.class);
    }

    @Input
    public Property<File> getFile() {
        return file;
    }

    @TaskAction
    public void taskAction() throws IOException, InterruptedException {
        Process startKube = new ProcessBuilder("kubectl", "apply", "-f", file.get().getAbsolutePath())
                .start();

        Logger logger = getProject().getLogger();

        int processCode = startKube.waitFor();
        if (processCode != 0) {
            String e = IOUtils.toString(startKube.getErrorStream(), Charset.forName("UTF-8"));
            logger.error("Couldn't start Pod: " + e);
            throw new GradleException(e);
        }

        String s = IOUtils.toString(startKube.getInputStream(), Charset.forName("UTF-8"));
        logger.info(s);
    }
}
