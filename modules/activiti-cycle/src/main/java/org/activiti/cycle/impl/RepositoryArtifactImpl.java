/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.cycle.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cycle.ArtifactAwareParameterizedAction;
import org.activiti.cycle.ArtifactType;
import org.activiti.cycle.ParameterizedAction;
import org.activiti.cycle.RepositoryArtifact;
import org.activiti.cycle.RepositoryArtifactOpenLinkAction;
import org.activiti.cycle.RepositoryConnector;

/**
 * Information about an artifact contained in the repository (e.g. a file,
 * signavio model, ...)
 * 
 * @author bernd.ruecker@camunda.com
 */
public class RepositoryArtifactImpl extends RepositoryNodeImpl implements RepositoryArtifact {

  private static final long serialVersionUID = 1L;

  private final ArtifactType artifactType;

  private final List<RepositoryArtifactOpenLinkAction> openLinkActions;

  public RepositoryArtifactImpl(String connectorId, String nodeId, ArtifactType artifactType, RepositoryConnector connector) {
    super(connectorId, nodeId);
    this.artifactType = artifactType;
    this.openLinkActions = artifactType.createOpenLinkActions(connector, this);
  }

  public String toString() {
    return this.getClass().getSimpleName() + " [connectorId=" + getConnectorId() + ";artifactId=" + getNodeId() + ";type=" + artifactType + ";metadata="
            + getMetadata() + "]";
  }

  public ArtifactType getArtifactType() {
    return artifactType;
  }

  public List<RepositoryArtifactOpenLinkAction> getOpenLinkActions() {
    return openLinkActions;
  }

  public List<ParameterizedAction> getParameterizedActions() {
    List<ParameterizedAction> actions = getArtifactType().getParameterizedActions();
    List<ParameterizedAction> filteredActions = new ArrayList<ParameterizedAction>();
    // filter actions not applicable to this artifact.
    for (ParameterizedAction parameterizedAction : actions) {
      if (parameterizedAction instanceof ArtifactAwareParameterizedAction) {
        ArtifactAwareParameterizedAction artifactAwareAction = (ArtifactAwareParameterizedAction) parameterizedAction;
        if (artifactAwareAction.isApplicable(this)) {
          filteredActions.add(parameterizedAction);
        }
      } else {
        filteredActions.add(parameterizedAction);
      }
    }
    return filteredActions;
  }
}