package io.muserver.rest;

import org.junit.Test;

import java.net.URI;

import static io.muserver.rest.UriPattern.uriTemplateToRegex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UriPatternTest {

    @Test
    public void leadingAndTrailingSlashesAreIgnored() {
        assertThat(pattern("/fruit"), equalTo(pattern("fruit")));
        assertThat(pattern("/fruit"), equalTo(pattern("fruit/")));
        assertThat(pattern("/fruit"), equalTo(pattern("/fruit/")));
        assertThat(literalCount("/fruit"), equalTo(literalCount("fruit")));
        assertThat(literalCount("/fruit"), equalTo(literalCount("fruit/")));
        assertThat(literalCount("/fruit"), equalTo(literalCount("/fruit/")));

        assertThat(uriTemplateToRegex("fruit").matcher(URI.create("fruit")).prefixMatches(), is(true));
    }

    @Test
    public void encodingIsOptional() {
        assertThat(pattern("fruit bits"), equalTo(pattern("fruit%20bits")));
    }
    @Test
    public void spacesInPathsAreOkay() {
        assertThat(uriTemplateToRegex("fruit bits").matcher(URI.create("fruit%20bits")).prefixMatches(), is(true));
    }


    @Test
    public void uriTemplatesCanBeConvertedToRegexes() {
        assertThat(pattern("/fruit"), equalTo("\\Qfruit\\E(/.*)?"));
        assertThat(pattern("/fruit/{name}"), equalTo("\\Qfruit\\E/(?<name>[^/]+?)(/.*)?"));
        assertThat(pattern("/fruit/{version : v[12]}"), equalTo("\\Qfruit\\E/(?<version>v[12])(/.*)?"));
        assertThat(pattern("/fruit/{version:v[12]}"), equalTo("\\Qfruit\\E/(?<version>v[12])(/.*)?"));
        assertThat(pattern("/fruit/{version: v[12]}/{name}/eat"), equalTo("\\Qfruit\\E/(?<version>v[12])/(?<name>[^/]+?)/\\Qeat\\E(/.*)?"));
    }

    @Test
    public void countsNumberOfLiteralCharacters() {
        assertThat(literalCount("fruit"), is(5));
        assertThat(literalCount("fruit/{name}"), is(6));
        assertThat(literalCount("fruit/{version : v[12]}"), is(6));
        assertThat(literalCount("fruit/{version:v[12]}"), is(6));
        assertThat(literalCount("fruit/{version: v[12]}/{name}/eat"), is(11));
    }

    @Test
    public void namedGroupsAreReturnedInThePattern() {
        UriPattern pattern = uriTemplateToRegex("/fruit/{version: v[12]}/{name}/eat");
        assertThat(pattern.namedGroups(), containsInAnyOrder("name", "version"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfRegexPatternIsInvalid() {
        // TODO: make the error message easy to understand for app developers
        uriTemplateToRegex("/fruit/{version : v[12](?<blah}");
    }

    @Test
    public void multipleParamsCanExistWithinASingleSegment() {
        UriPattern uriPattern = uriTemplateToRegex("people/{familyName}-{givenName}");
        assertThat(uriPattern.namedGroups(), containsInAnyOrder("familyName", "givenName"));
        assertThat(uriPattern.numberOfLiterals, is(8));

        PathMatch matcher = uriPattern.matcher(URI.create("people/Fennel-Kennel"));
        assertThat(matcher.prefixMatches(), is(true));
        assertThat(matcher.params().get("givenName"), equalTo("Kennel"));
        assertThat(matcher.params().get("familyName"), equalTo("Fennel"));
    }

    @Test
    public void parametersCanBeRepeated() {
        UriPattern uriPattern = uriTemplateToRegex("/user/{ user}/umm/{ user }");
        assertThat(uriPattern.namedGroups(), contains("user"));
        assertThat(uriPattern.numberOfLiterals, is(10));
        PathMatch matcher = uriPattern.matcher(URI.create("/user/dan/umm/dan"));
        assertThat(matcher.prefixMatches(), is(true));
        assertThat(matcher.params().get("user"), is("dan"));
        matcher = uriPattern.matcher(URI.create("/user/dan/umm/notdan"));
        assertThat(matcher.prefixMatches(), is(false));
    }

    @Test
    public void canCheckForEqualityIgnoringVariableNames() {
        assertThat(templatesEqual("/fruit", "/.*"), is(false));
        assertThat(templatesEqual("/fruit", "/fruit"), is(true));
        assertThat(templatesEqual("/fruit/{id}", "/fruit/{name}"), is(true));
        assertThat(templatesEqual("/fruit/{id : .*}", "/fruit/{name:.*}"), is(true));
        assertThat(templatesEqual("/fruit/{id : [0-9]*}", "/fruit/{id : .*}"), is(false));
    }

    @Test
    public void canGetPathsWithoutRegexInfo() {
        assertThat(uriTemplateToRegex("/fruit").path, is("/fruit"));
        assertThat(uriTemplateToRegex("/fruit/{id}").path, is("/fruit/{id}"));
        assertThat(uriTemplateToRegex("/fruit/{id : .*}").path, is("/fruit/{id}"));
        assertThat(uriTemplateToRegex("/fruit/{id : [0-9]*}/b/{id2}/c/{ ha }").path, is("/fruit/{id}/b/{id2}/c/{ha}"));
    }

    private static boolean templatesEqual(String one, String two) {
        return uriTemplateToRegex(one).equalModuloVariableNames(uriTemplateToRegex(two));
    }

    private static String pattern(String template) {
        return uriTemplateToRegex(template).pattern();
    }
    private static int literalCount(String template) {
        return uriTemplateToRegex(template).numberOfLiterals;
    }


}