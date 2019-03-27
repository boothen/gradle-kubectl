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

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import uk.co.boothen.gradle.kubectl.extension.KubectlPluginExtension;
import uk.co.boothen.gradle.kubectl.extension.Pod;
import uk.co.boothen.gradle.kubectl.extension.PortForward;
import uk.co.boothen.gradle.kubectl.task.StartTask;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class KubectlPluginTest {

    @Test
    public void greetingTest(){
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");

        Task test = project.getTasks().findByName("test");
        KubectlPluginExtension kubectlPluginExtension = new KubectlPluginExtension();
        kubectlPluginExtension.setFile("kube.yaml");
        kubectlPluginExtension.setRequiredBy(test);
        Pod pod = new Pod("dynamodb");
        kubectlPluginExtension.setPods(Collections.singletonList(pod));
        PortForward portForward = new PortForward("dynamodb", 8000, 800);
        kubectlPluginExtension.setPortForwards(Collections.singletonList(portForward));

        project.getPluginManager().apply("uk.co.boothen.gradle.kubectl");

        assertThat(project.getPluginManager().hasPlugin("uk.co.boothen.gradle.kubectl")).isTrue();

        assertThat(project.getTasks().withType(StartTask.class)).isNotNull();
        assertThat(project.getTasks().withType(org.gradle.api.tasks.testing.Test.class)).isNotNull();
    }

}