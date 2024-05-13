package com.prosilion.presto;

import java.util.Properties;

/**
 * A bean of this class instantiates in either one of two ways:
 * 1) If container doesn't already have one, H2DatabaseAutoConfiguration.java
 *      will create one.
 * 2) Pre-existing by some other mechanism (currently does not occur)
 */
public class H2DatabaseConfig extends Properties {
  private static final long serialVersionUID = 5662570853707247891L;
}
