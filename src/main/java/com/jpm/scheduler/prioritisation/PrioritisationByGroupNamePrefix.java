/**
 * 
 */
package com.jpm.scheduler.prioritisation;

import com.jpm.scheduler.ConcreteMessage;

/**
 * This strategy prioritize the messages by group name prefix. In this
 * hypothetical scenario, by convention, each group name is composed by
 * concatenating the prefix, # and the group number.
 *
 * @author ccoca
 */
public class PrioritisationByGroupNamePrefix implements PrioritisationFilter {

    @Override
    public boolean match(ConcreteMessage message, String filter) {
        String messagePrefix = message.getGroupId().split("#")[0];
        String filterPrefix = filter.split("#")[0];
        return messagePrefix.equals(filterPrefix);
    }

}
