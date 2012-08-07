/*******************************************************************************
 * Copyright (c) 2012 pf_miles.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     pf_miles - initial API and implementation
 ******************************************************************************/
package com.github.pfmiles.dropincc.testhelper;

import java.util.List;
import java.util.Map;

import com.github.pfmiles.dropincc.impl.CAlternative;
import com.github.pfmiles.dropincc.impl.EleType;
import com.github.pfmiles.dropincc.impl.GruleType;
import com.github.pfmiles.dropincc.impl.kleene.KleeneType;

/**
 * @author pf-miles
 * 
 */
public class AnalyzedLangForTest {

    public Map<KleeneType, List<EleType>> kleeneTypeToNode;
    public Map<GruleType, List<CAlternative>> ruleTypeToAlts;

}
