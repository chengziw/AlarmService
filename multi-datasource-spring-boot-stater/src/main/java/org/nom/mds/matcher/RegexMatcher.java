package org.nom.mds.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegexMatcher implements Matcher {
    private String pattern;
    private String ds;
}

