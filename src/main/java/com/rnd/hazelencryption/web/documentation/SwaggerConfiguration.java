package com.rnd.hazelencryption.web.documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * The type Swagger configuration.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Configuration for Swagger API.
     *
     * @param apiInfo the api info
     * @return docket
     */
    @Bean(name="swaggerApi")
    public Docket scrabbleApi(@Qualifier("metaData") ApiInfo apiInfo) {
        logger.info("Entering into Config ::");
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rnd.hazelencryption"))
                .paths(regex("/.*"))
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .apiInfo(apiInfo);
    }

    /**
     * Meta Data defination which will be displayed in Swagger API
     *
     * @param contact the contact
     * @return api info
     */
    @Bean("metaData")
    public ApiInfo metaData(@Qualifier("contact") Contact contact) {
        logger.info("Entering into MetaData ::");
        StringVendorExtension[] vendorExtension = {new StringVendorExtension("Apache License","https://www.apache.org/licenses/LICENSE-2.0")};
        ApiInfo apiInfo = new ApiInfo(
                "Demo application for  Encryption of Hazelcast",
                "Demo application for  Encryption of Hazelcast",
                "1.0",
                "Terms of service",
                 contact,
                "Incredibles",
                "spend-incredibles@concur.com",
                Arrays.asList(vendorExtension)
        );
        return apiInfo;
    }


    /**
     * Contact contact.
     *
     * @return the contact
     */
    @Bean("contact")
    public Contact contact(){
        return new Contact("Incredibles","spend-incredibles@concur.com","spend-incredibles@concur.com");
    }



}
