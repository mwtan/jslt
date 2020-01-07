package com.schibsted.spt.data.jslt.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.schibsted.spt.data.jslt.JsltException;

public class ForTextExpression extends ForExpression {

	public ForTextExpression(ExpressionNode valueExpr, LetExpression[] lets, ExpressionNode loopExpr,
			ExpressionNode ifExpr, Location location) {
		super(valueExpr, lets, loopExpr, ifExpr, location);
	}

	public JsonNode apply(Scope scope, JsonNode input) {
		JsonNode array = valueExpr.apply(scope, input);
		if (array.isNull()) {
			return NullNode.instance;
		}
		else if (array.isObject()) {
			array = NodeUtils.convertObjectToArray(array);
		}
		else if (!array.isArray()) {
			throw new JsltException("For loop can't iterate over " + array, location);
		}
		StringBuilder sb = new StringBuilder();
		for (int ix = 0; ix < array.size(); ix++) {
			JsonNode value = array.get(ix);
			// must evaluate lets over again for each value because of context
			if (lets.length > 0) {
				NodeUtils.evalLets(scope, value, lets);
			}
			if (ifExpr == null || NodeUtils.isTrue(ifExpr.apply(scope, value))) {
				JsonNode jnode = loopExpr.apply(scope, value);
				sb.append(jnode.textValue());
			}
		}
		return new TextNode(sb.toString());
	}
}
