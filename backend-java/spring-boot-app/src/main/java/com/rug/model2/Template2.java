package com.rug.model2;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.Formats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.datatype.XMLGregorianCalendar;



public class Template2 {

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VAR_PREFIX = "var";

    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String VARGEN_PREFIX ="vargen";

    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX ="tmpl";

    public static final String TW_NS = "http://twitter.com/";
    public static final String TW_PREFIX = "tw";

    private final ProvFactory pFactory;
    private final Namespace ns;

    public enum TweetType {
        ORIGINAL,
        REACTION
    }

    public Template2(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(VAR_PREFIX, VAR_NS);
        ns.register(VARGEN_PREFIX, VARGEN_NS);
        ns.register(TMPL_PREFIX, TMPL_NS);
        ns.register(TW_PREFIX, TW_NS);
    }


    public ProvFactory getpFactory() {
        return pFactory;
    }

    public QualifiedName qn_prov(String n) {
        return ns.qualifiedName("prov", n, pFactory);
    }

    public QualifiedName qn_var(String n) {
        return ns.qualifiedName(VAR_PREFIX, n, pFactory);
    }

    public QualifiedName qn_vargen(String n) {
        return ns.qualifiedName(VARGEN_PREFIX, n, pFactory);
    }

    public QualifiedName qn_tw(String n) {
        return ns.qualifiedName(TW_PREFIX, n, pFactory);
    }

    public QualifiedName qn_tmpl(String n) {
        return ns.qualifiedName(TMPL_PREFIX, n, pFactory);
    }


    public void saveDocument(Document document, String file_provn, String file_svg, String file_png) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file_provn, Formats.ProvFormat.PROVN, document);
        intF.writeDocument(file_svg, Formats.ProvFormat.SVG, document);
        // intF.writeDocument(file_png, Formats.ProvFormat.PNG, document);
    }


    public Collection<Attribute> createOriginalAuthorProps(ProvFactory pFactory) {
        Collection<Attribute> authorProps = new ArrayList<>();
        Attribute authorPropsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "ORIGINAL USER PROPERTIES", pFactory.getName().XSD_STRING);
        Attribute authorPropsCredible = pFactory.newAttribute(qn_tw("credible"), qn_var("ORIGINAL_credible"), pFactory.getName().XSD_INT);
        Attribute authorPropsUsername = pFactory.newAttribute(qn_tw("username"), qn_var("ORIGINAL_username"), pFactory.getName().XSD_STRING);
        Attribute authorPropsVerified = pFactory.newAttribute(qn_tw("verified"), qn_var("ORIGINAL_verified"), pFactory.getName().XSD_BOOLEAN);
        Attribute authorPropsFollowersCount = pFactory.newAttribute(qn_tw("followers_count"), qn_var("ORIGINAL_followers_count"), pFactory.getName().XSD_INT);
        Attribute authorPropsFollowingCount = pFactory.newAttribute(qn_tw("following_count"), qn_var("ORIGINAL_following_count"), pFactory.getName().XSD_INT);

        authorProps.addAll(Arrays.asList(authorPropsType,
                                         authorPropsCredible,
                                         authorPropsUsername,
                                         authorPropsVerified,
                                         authorPropsFollowersCount,
                                         authorPropsFollowingCount));

        return authorProps;
    }

        
    public Document createTemplateDocument() {

        // 1. ENTITY - entity_originalTweet
        Collection<Attribute> originalTweetAttributes = new ArrayList<>();
        Attribute originalTweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("original_text"), pFactory.getName().XSD_ANY_URI);
        Attribute originalTweetType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "ORIGINAL TWEET", pFactory.getName().XSD_STRING);
        Attribute originalTweetCreatedAt = pFactory.newAttribute(qn_tw("created at"), qn_var("ORIGINAL_created_at"), pFactory.getName().XSD_STRING);
        Attribute originalTweetLocation = pFactory.newAttribute(qn_tw("location"), qn_var("ORIGINAL_location"), pFactory.getName().XSD_STRING);
        originalTweetAttributes.addAll(Arrays.asList(originalTweetValue, originalTweetType, originalTweetCreatedAt, originalTweetLocation));
        Entity entity_originalTweet = pFactory.newEntity(qn_var("original_tweet_id"), originalTweetAttributes);


        // 2. ACTIVITY - activity_post
        Collection<Attribute> postActivityAttributes = new ArrayList<>();
        Attribute postType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        postActivityAttributes.add(postType);
        Activity activity_post = pFactory.newActivity(qn_var("post_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, postActivityAttributes);
        

        // 3. GENERATION - gen1
        WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(null, entity_originalTweet.getId(), activity_post.getId());


        // 4. AGENT - agent_originalAuthor
        Collection<Attribute> originalAuthorAttributes = new ArrayList<>();
        Attribute originalAuthorType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_prov("Person"), pFactory.getName().XSD_STRING);
        Attribute originalAuthorName = pFactory.newAttribute(qn_tw("name"), qn_var("ag_o_name"), pFactory.getName().XSD_STRING);
        originalAuthorAttributes.addAll(Arrays.asList(originalAuthorType, originalAuthorName));
        Agent agent_originalAuthor = pFactory.newAgent(qn_var("original_author_id"), originalAuthorAttributes);


        // 5. ASSOCIATION - assoc1
        Collection<Attribute> assoc1Attributes = new ArrayList<>();
        Attribute assoc1Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "author", pFactory.getName().XSD_STRING);
        assoc1Attributes.add(assoc1Role);
        WasAssociatedWith assoc1 = pFactory.newWasAssociatedWith(null, activity_post.getId(), agent_originalAuthor.getId(), (QualifiedName)null, assoc1Attributes);


        // 14. ENTITY - entity_originalAuthorProps
        Entity entity_originalAuthorProps = pFactory.newEntity(qn_var("original_author_props_id"), createOriginalAuthorProps(pFactory));

        
        // 15. ATTRIBUTION - attr1
        WasAttributedTo attrib1 = pFactory.newWasAttributedTo(null, entity_originalAuthorProps.getId(), agent_originalAuthor.getId());

        // 16. ACTIVITY - activity_react
        Collection<Attribute> reactActivityAttributes = new ArrayList<>();
        Attribute reactType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        Attribute reactLinked = pFactory.newAttribute(qn_tmpl("linked"), qn_var("reaction_group_of_tweets_id"), pFactory.getName().XSD_STRING);
        reactActivityAttributes.addAll(Arrays.asList(reactType, reactLinked));
        Activity activity_react = pFactory.newActivity(qn_var("react_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, reactActivityAttributes);

        // 17. COMMUNICATION - wasInformedBy1
        WasInformedBy wasInformedBy1 = pFactory.newWasInformedBy(null, qn_var("react_id"), qn_var("post_id"));


        // 18. AGENT - agent_reactionAuthor
        Collection<Attribute> reactionAuthorsAttributes = new ArrayList<>();
        Attribute reactionAuthorsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "GROUP OF USERS", pFactory.getName().XSD_STRING);
        Attribute reactionAuthorsNrOfDistinctAuthors = pFactory.newAttribute(qn_tw("number_of_distinct_authors"), qn_var("nr_of_distinct_authors"), pFactory.getName().XSD_INT);
        Attribute reactionAuthorsLinked = pFactory.newAttribute(qn_tmpl("linked"), qn_var("react_id"), pFactory.getName().XSD_STRING);
        reactionAuthorsAttributes.addAll(Arrays.asList(reactionAuthorsType, reactionAuthorsNrOfDistinctAuthors, reactionAuthorsLinked));
        Agent agent_reactionAuthors = pFactory.newAgent(qn_var("reaction_group_of_authors_id"), reactionAuthorsAttributes);


        // 19. ASSOCIATION - assoc3
        Collection<Attribute> assoc3Attributes = new ArrayList<>();
        Attribute assoc3Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "authors", pFactory.getName().XSD_STRING);
        assoc3Attributes.add(assoc3Role);
        WasAssociatedWith assoc3 = pFactory.newWasAssociatedWith(null, activity_react.getId(), agent_reactionAuthors.getId(), (QualifiedName)null, assoc3Attributes);


        // 22. ENTITY - entity_reactionTweet
        Collection<Attribute> reactionTweetsAttributes = new ArrayList<>();
        Attribute reactionTweetsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "GROUP OF REACTION TWEETS", pFactory.getName().XSD_STRING);
        Attribute reactionTweetsTimeInterval = pFactory.newAttribute(qn_tw("time_interval"), qn_var("time_interval"), pFactory.getName().XSD_STRING);
        Attribute reactionTweetsNumberOfReactions = pFactory.newAttribute(qn_tw("number_of_reactions"), qn_var("nr_of_reactions"), pFactory.getName().XSD_INT);
        Attribute reactionTweetsPercentageOfTotalReactions = pFactory.newAttribute(qn_tw("percentage_out_of_total_reactions"), qn_var("percentage_out_of_total_reactions"), pFactory.getName().XSD_INT);
        Attribute reactionTweetsNrOfReplies = pFactory.newAttribute(qn_tw("number_of_replies"), qn_var("nr_of_replies"), pFactory.getName().XSD_INT);
        Attribute reactionTweetsNrOfQuotes = pFactory.newAttribute(qn_tw("number_of_quotes"), qn_var("nr_of_quotes"), pFactory.getName().XSD_INT);
        Attribute reactionTweetsNrOfRetweets = pFactory.newAttribute(qn_tw("number_of_retweets"), qn_var("nr_of_retweets"), pFactory.getName().XSD_INT);
        reactionTweetsAttributes.addAll(Arrays.asList(reactionTweetsType, 
                                                      reactionTweetsTimeInterval, 
                                                      reactionTweetsNumberOfReactions,
                                                      reactionTweetsPercentageOfTotalReactions, 
                                                      reactionTweetsNrOfReplies, 
                                                      reactionTweetsNrOfQuotes, 
                                                      reactionTweetsNrOfRetweets));
        Entity entity_reactionTweets = pFactory.newEntity(qn_var("reaction_group_of_tweets_id"), reactionTweetsAttributes);


        // 23. GENERATION - gen3
        WasGeneratedBy gen3 = pFactory.newWasGeneratedBy(null, entity_reactionTweets.getId(), activity_react.getId());
        

        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.addAll(Arrays.asList(entity_originalTweet, 
                                                 activity_post, 
                                                 agent_originalAuthor, 
                                                 gen1, 
                                                 assoc1,
                                                 entity_originalAuthorProps,
                                                 attrib1,
                                                 activity_react,
                                                 wasInformedBy1,
                                                 agent_reactionAuthors,
                                                 assoc3,
                                                 entity_reactionTweets,
                                                 gen3));



        Bundle bundle = pFactory.newNamedBundle(qn_vargen("bundle_id"), ns, statementCollection);

        Document document = pFactory.newDocument();
        document.getStatementOrBundle().add(bundle);


        return document;
    }


    public static void main( String[] args )
    {
        if (args.length!=3) throw new UnsupportedOperationException("main to be called with 3 filenames");
        String file_provn=args[0];
        String file_svg=args[1];
        String file_png=args[2];
        
        Template2 template = new Template2(InteropFramework.getDefaultFactory());

        Document document = template.createTemplateDocument();
        template.saveDocument(document, file_provn, file_svg, file_png);
    }

}
