package com.schibsted.spt.data.jslt.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * An ExpressionList holds a single ExpressionNode or a collection of ExpressionNodes.
 * When a collection of ExpressionNodes exist, each expression is applied to the input
 * and their result is concatenated as a string and returned as a TextNode. 
 * <p>
 * @author Will Tan
 */
public class ExpressionList extends AbstractNode {
	private List<ExpressionNode> elist = new ArrayList<ExpressionNode>(128);
	
	public ExpressionList(Location location) {
		super(location);
	}

	@Override
	public JsonNode apply(Scope scope, JsonNode input) {
		if (elist.size() == 1) {
			return elist.get(0).apply(scope, input);
		}
		else {
			StringBuilder sb = new StringBuilder();
			Iterator<ExpressionNode> it = elist.iterator();
			while (it.hasNext()) {
				ExpressionNode node = it.next();
				JsonNode jnode = node.apply(scope, input);
				sb.append(jnode.asText());
			}
			return new TextNode(sb.toString());
		}
	}

	public void add(ExpressionNode expr) {
		elist.add(expr);
	}
	
	public Iterator<ExpressionNode> getNodes() {
		return elist.iterator();
	}
	
	public ExpressionNode optimize() {
		List<ExpressionNode> newelist = new ArrayList<ExpressionNode>(128);
		Iterator<ExpressionNode> it = getNodes();
		while (it.hasNext()) {
			ExpressionNode node = it.next();
			newelist.add(node.optimize());
		}
		elist = newelist;
		return this;
	}
	
	public void computeMatchContexts(DotExpression parent) {
		Iterator<ExpressionNode> it = getNodes();
		while (it.hasNext()) {
			ExpressionNode node = it.next();
			node.computeMatchContexts(parent);
		}
	}

	public List<ExpressionNode> getChildren() {
		List<ExpressionNode> children = new ArrayList<ExpressionNode>(64);
		Iterator<ExpressionNode> it = getNodes();
		while (it.hasNext()) {
			ExpressionNode node = it.next();
			children.add(node);
		}
		return children;
	}

	public String toString() {
		if (elist.size() == 1) {
			return elist.get(0).toString();
		}
		else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < elist.size(); i++) {
				ExpressionNode node = elist.get(i);
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(node.toString());
			}
			return sb.toString();
		}
	}
}
