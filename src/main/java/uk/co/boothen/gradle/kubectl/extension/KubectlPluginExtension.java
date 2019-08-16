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

import org.gradle.api.Action;
import org.gradle.api.Task;

import java.util.ArrayList;
import java.util.List;

public class KubectlPluginExtension {

    private String file;
    private Task requiredBy;
    private boolean forceStopBeforeStart;
    private List<Pod> pod = new ArrayList<>();
    private List<PortForward> portForward = new ArrayList<>();

    public String getFile() {
        return file;
    }

    public Task getRequiredBy() {
        return requiredBy;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setRequiredBy(Task requiredBy) {
        this.requiredBy = requiredBy;
    }

    public boolean isForceStopBeforeStart() {
        return forceStopBeforeStart;
    }

    public void setForceStopBeforeStart(boolean forceStopBeforeStart) {
        this.forceStopBeforeStart = forceStopBeforeStart;
    }

    public List<Pod> getPod() {
        return pod;
    }

    public void pod(Action<? super Pod> action) {
        Pod pod = new Pod();
        action.execute(pod);
        this.pod.add(pod);
    }

    public List<PortForward> getPortForward() {
        return portForward;
    }

    public void portForward(Action<? super PortForward> action) {
        PortForward portForward = new PortForward();
        action.execute(portForward);
        this.portForward.add(portForward);
    }

    public void setPods(List<Pod> pods) {
        pod = pods;
    }

    public void setPortForwards(List<PortForward> portForwards) {
        portForward = portForwards;
    }
}
