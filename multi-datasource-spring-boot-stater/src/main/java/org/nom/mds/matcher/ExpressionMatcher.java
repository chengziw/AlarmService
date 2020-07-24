package org.nom.mds.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExpressionMatcher implements Matcher {
    private String expression;
    private String ds;
}
