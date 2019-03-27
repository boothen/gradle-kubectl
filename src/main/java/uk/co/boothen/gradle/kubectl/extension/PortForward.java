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

public class PortForward {

    private String service;
    private Integer port;
    private Integer targetPort;

    public PortForward(String service, Integer port, Integer targetPort) {
        this.service = service;
        this.port = port;
        this.targetPort = targetPort;
    }

    public PortForward() {

    }

    public String getService() {
        return service;
    }

    public void service(String service) {
        this.service = service;
    }

    public Integer getPort() {
        return port;
    }

    public void port(Integer port) {
        this.port = port;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void targetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }
}
