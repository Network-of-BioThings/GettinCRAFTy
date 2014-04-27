/*
 *  IndexFileWordNetImpl.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 */

package gate.wordnet;

import gate.Gate;
import gate.Resource;
import gate.creole.AbstractLanguageResource;
import gate.creole.ResourceData;
import gate.creole.ResourceInstantiationException;
import gate.util.Err;

import java.net.URL;

public class IndexFileWordNetImpl extends AbstractLanguageResource {

  public void setPropertyUrl(URL u) {
    // ignore this
  }

  @Override
  public Resource init() throws ResourceInstantiationException {

    ResourceData rd = Gate.getCreoleRegister().get(getClass().getName());

    Err
            .println("WordNet support has been upgraded requiring changes to the configuration files.\n"
                    + "Please see "
                    + rd.getHelpURL()
                    + " for details on how to update your application.");

    throw new ResourceInstantiationException("WordNet support has been upgraded");
  }
}
