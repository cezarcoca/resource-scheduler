/**
 * 
 */
package com.jpm.scheduler.prioritisation;

import com.jpm.scheduler.ConcreteMessage;

/**
 * @author ccoca
 *
 */
public class PrioritisationByGroupName implements PrioritisationFilter {

    @Override
    public boolean match(ConcreteMessage message, String filter) {
        return message.getGroupId().equals(filter);
    }

}
