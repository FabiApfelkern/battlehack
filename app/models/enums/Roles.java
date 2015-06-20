package models.enums;

import com.avaje.ebean.annotation.EnumMapping;

@EnumMapping(nameValuePairs="customer=0, owner=1")
public enum Roles
{
    customer,
    owner
}