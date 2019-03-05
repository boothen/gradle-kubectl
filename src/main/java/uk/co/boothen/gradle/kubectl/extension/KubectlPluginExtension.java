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

package uk.co.boothen.gradle.kubectl.extension;

import org.gradle.api.Task;

public class KubectlPluginExtension {

    private String podName;
    private String file;
    private Task requiredBy;
    private Integer portForward;

    public String getPodName() {
        return podName;
    }

    public String getFile() {
        return file;
    }

    public Task getRequiredBy() {
        return requiredBy;
    }

    public Integer getPortForward() {
        return portForward;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setRequiredBy(Task requiredBy) {
        this.requiredBy = requiredBy;
    }

    public void setPortForward(Integer portForward) {
        this.portForward = portForward;
    }
}
