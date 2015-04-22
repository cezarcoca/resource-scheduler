/**
 * 
 */
package com.jpm.scheduler.prioritisation;

import com.jpm.scheduler.ConcreteMessage;

/**
 * This strategy prioritize the messages by group name.
 *
 * @author ccoca
 */
public class PrioritisationByGroupName implements PrioritisationFilter {

    @Override
    public boolean match(ConcreteMessage message, String filter) {
        return message.getGroupId().equals(filter);
    }

}
