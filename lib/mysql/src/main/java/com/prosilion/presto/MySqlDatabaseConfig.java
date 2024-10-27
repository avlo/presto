package com.prosilion.presto;

import java.util.Properties;

/**
 * A bean of this class instantiates in either one of two ways:
 * 1) If container doesn't already have one, MySqlDatabaseAutoConfiguration.java
 *      will create one.
 * 2) Pre-existing by some other mechanism (currently does not occur)
 */
public class MySqlDatabaseConfig extends Properties {
  private static final long serialVersionUID = -3948492262421862288L;
}
