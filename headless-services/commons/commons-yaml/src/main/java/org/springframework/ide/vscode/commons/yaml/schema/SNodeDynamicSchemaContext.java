/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.yaml.schema;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.ide.vscode.commons.util.CollectionUtil;
import org.springframework.ide.vscode.commons.util.Log;
import org.springframework.ide.vscode.commons.util.text.IDocument;
import org.springframework.ide.vscode.commons.yaml.path.YamlPath;
import org.springframework.ide.vscode.commons.yaml.structure.YamlStructureParser.SChildBearingNode;
import org.springframework.ide.vscode.commons.yaml.structure.YamlStructureParser.SKeyNode;
import org.springframework.ide.vscode.commons.yaml.structure.YamlStructureParser.SNode;

/**
 * Adapts an SNode so it can be used by a YamlSchema as a {@link DynamicSchemaContext}
 *
 * @author Kris De Volder
 */
public class SNodeDynamicSchemaContext extends CachingSchemaContext {

	private SNode contextNode;
	private YamlPath contextPath;

	public SNodeDynamicSchemaContext(SNode contextNode, YamlPath contextPath) {
		this.contextNode = contextNode;
		this.contextPath = contextPath;
	}

	@Override
	protected Set<String> computeDefinedProperties() {
		try {
			if (contextNode instanceof SChildBearingNode) {
				List<SNode> children = ((SChildBearingNode)contextNode).getChildren();
				if (CollectionUtil.hasElements(children)) {
					Set<String> keys = new HashSet<>(children.size());
					for (SNode c : children) {
						if (c instanceof SKeyNode) {
							keys.add(((SKeyNode) c).getKey());
						}
					}
					return keys;
				}
			}
		} catch (Exception e) {
			Log.log(e);
		}
		return Collections.emptySet();
	}

	@Override
	public IDocument getDocument() {
		return contextNode.getDocument();
	}

	@Override
	public YamlPath getPath() {
		return contextPath;
	}

	@Override
	public String toString() {
		return "SNodeDynamicSchemaContext("+contextPath+")";
	}

}
