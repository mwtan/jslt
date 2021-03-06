
// Copyright 2018 Schibsted Marketplaces Products & Technology As
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.schibsted.spt.data.jslt.cli;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

public class JSLT {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage: java com.schibsted.spt.data.jslt.cli.JSLT <jslt file> <json input file>");
      System.exit(1);
    }

    Expression expr = Parser.compile(new File(args[0]));
    // if (expr instanceof ExpressionImpl)
    //   ((ExpressionImpl) expr).dump();

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
    
    JsonNode input = mapper.readTree(new File(args[1]));

    JsonNode output = expr.apply(input);

    if (output == null)
      System.out.println("WARN: returned Java null!");
    String out = StringEscapeUtils.unescapeJava(mapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(output));
    out = StringUtils.removeStart(out, "\"");
    out = StringUtils.removeEnd(out, "\"");
    System.out.println(out);
  }

}
