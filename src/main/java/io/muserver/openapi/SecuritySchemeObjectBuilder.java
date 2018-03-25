package io.muserver.openapi;

import java.net.URI;

/**
 * <p>Defines a security scheme that can be used by the operations. Supported schemes are HTTP authentication, an API key
 * (either as a header or as a query parameter), OAuth2's common flows (implicit, password, application and access code)
 * as defined in <a href="https://tools.ietf.org/html/rfc6749">RFC6749</a>, and
 * <a href="https://tools.ietf.org/html/draft-ietf-oauth-discovery-06">OpenID Connect Discovery</a>.</p>
 */
public class SecuritySchemeObjectBuilder {
    private String type;
    private String description;
    private String name;
    private String in;
    private String scheme;
    private String bearerFormat;
    private OAuthFlowsObject flows;
    private URI openIdConnectUrl;

    /**
     * @param type <strong>REQUIRED</strong>. The type of the security scheme. Valid values are <code>"apiKey"</code>,
     *             <code>"http"</code>, <code>"oauth2"</code>, <code>"openIdConnect"</code>.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @param description A short description for security scheme. <a href="http://spec.commonmark.org/">CommonMark syntax</a>
     *                    MAY be used for rich text representation.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param name <strong>REQUIRED (when type is apiKey)</strong>. The name of the header, query or cookie parameter to be used.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @param in <strong>REQUIRED (when type is apiKey)</strong>. The location of the API key. Valid values are <code>"query"</code>,
     *           <code>"header"</code> or <code>"cookie"</code>.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withIn(String in) {
        this.in = in;
        return this;
    }

    /**
     * @param scheme <strong>REQUIRED (when type is http)</strong>. The name of the HTTP Authorization scheme to be used in the
     *               <a href="https://tools.ietf.org/html/rfc7235#section-5.1">Authorization header as defined in RFC7235</a>.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * @param bearerFormat A hint to the client to identify how the bearer token is formatted. Bearer tokens are usually
     *                     generated by an authorization server, so this information is primarily for documentation purposes.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withBearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
        return this;
    }

    /**
     * @param flows <strong>REQUIRED (when type is oauth2)</strong>. An object containing configuration information for the flow types supported.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withFlows(OAuthFlowsObject flows) {
        this.flows = flows;
        return this;
    }

    /**
     * @param openIdConnectUrl <strong>REQUIRED (when type is openIdConnect)</strong>. OpenId Connect URL to discover OAuth2 configuration values.
     *                         This MUST be in the form of a URL.
     * @return The current builder
     */
    public SecuritySchemeObjectBuilder withOpenIdConnectUrl(URI openIdConnectUrl) {
        this.openIdConnectUrl = openIdConnectUrl;
        return this;
    }

    public SecuritySchemeObject build() {
        return new SecuritySchemeObject(type, description, name, in, scheme, bearerFormat, flows, openIdConnectUrl);
    }

    /**
     * Creates a builder for a {@link SecuritySchemeObject}
     *
     * @return A new builder
     */
    public static SecuritySchemeObjectBuilder securityScheme() {
        return new SecuritySchemeObjectBuilder();
    }
}