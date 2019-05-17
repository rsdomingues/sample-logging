package com.fooapp.cart.usecase;

import com.fooapp.cart.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.*;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@Component
public class ValidateItem {

    private Validator validator;

    public ValidateItem() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void execute(Item item) {
        checkArgument(item != null, "Invalid item");
        log.info("checking item with sku:{}, amount:{} and price:{}", value("sku", item.getSku()),
                value("amount", item.getAmount()),
                value("unitPrice", item.getUnitPrice()));

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        if(violations.isEmpty()) {
            log.info("Item sku:{}, amout:{} and price:{} is valid", value("sku", item.getSku()),
                    value("amount", item.getAmount()),
                    value("unitPrice", item.getUnitPrice()));
        } else {
            StringBuilder builder = new StringBuilder();
            violations.stream().map(ConstraintViolation::getMessage).forEach(builder::append);

            log.info("Item sku:{}, amout:{} and price:{} is invalid because: {}", value("sku", item.getSku()),
                value("amount", item.getAmount()),
                value("unitPrice", item.getUnitPrice()),
                value("error", builder.toString()));

            checkArgument(false, "Invalid item: " + builder.toString());
        }
    }
}
