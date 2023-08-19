package com.rug.model2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataClass2 {

    // Add the DataClass instance variables and getters/setters
    @JsonProperty("original")
    private Original original;

    @JsonProperty("group_of_reactions")
    private GroupOfReactions groupOfReactions;

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public GroupOfReactions getGroupOfReactions() {
        return groupOfReactions;
    }

    public void setGroupOfReactions(GroupOfReactions groupOfReactions) {
        this.groupOfReactions = groupOfReactions;
    }

    
    public static class Original {
        @JsonProperty("original_tweet_id")
        private String originalTweetId;

        @JsonProperty("original_text")
        private String originalText;

        @JsonProperty("ORIGINAL_created_at")
        private String originalCreatedAt;

        @JsonProperty("ORIGINAL_location")
        private String originalLocation;

        @JsonProperty("post_id")
        private String postId;

        @JsonProperty("original_author_id")
        private String originalAuthorId;

        @JsonProperty("ag_o_name")
        private String agOName;

        @JsonProperty("original_author_props_id")
        private String originalAuthorPropsId;

        @JsonProperty("ORIGINAL_credible")
        private String originalCredible;

        @JsonProperty("ORIGINAL_username")
        private String originalUsername;

        @JsonProperty("ORIGINAL_verified")
        private String originalVerified;

        @JsonProperty("ORIGINAL_followers_count")
        private String originalFollowersCount;

        @JsonProperty("ORIGINAL_following_count")
        private String originalFollowingCount;

        // Add getters and setters
        // Original Tweet ID
        public String getOriginalTweetId() {
            return originalTweetId;
        }

        public void setOriginalTweetId(String originalTweetId) {
            this.originalTweetId = originalTweetId;
        }

        // Original Text
        public String getOriginalText() {
            return originalText;
        }

        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }

        // ORIGINAL Created At
        public String getOriginalCreatedAt() {
            return originalCreatedAt;
        }

        public void setOriginalCreatedAt(String originalCreatedAt) {
            this.originalCreatedAt = originalCreatedAt;
        }

        // ORIGINAL Location
        public String getOriginalLocation() {
            return originalLocation;
        }

        public void setOriginalLocation(String originalLocation) {
            this.originalLocation = originalLocation;
        }

        // Post ID
        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        // Original Author ID
        public String getOriginalAuthorId() {
            return originalAuthorId;
        }

        public void setOriginalAuthorId(String originalAuthorId) {
            this.originalAuthorId = originalAuthorId;
        }

        // AG O Name
        public String getAgOName() {
            return agOName;
        }

        public void setAgOName(String agOName) {
            this.agOName = agOName;
        }

        // Original Author Props ID
        public String getOriginalAuthorPropsId() {
            return originalAuthorPropsId;
        }

        public void setOriginalAuthorPropsId(String originalAuthorPropsId) {
            this.originalAuthorPropsId = originalAuthorPropsId;
        }

        // ORIGINAL Credible
        public String getOriginalCredible() {
            return originalCredible;
        }

        public void setOriginalCredible(String originalCredible) {
            this.originalCredible = originalCredible;
        }

        // ORIGINAL Username
        public String getOriginalUsername() {
            return originalUsername;
        }

        public void setOriginalUsername(String originalUsername) {
            this.originalUsername = originalUsername;
        }

        // ORIGINAL Verified
        public String getOriginalVerified() {
            return originalVerified;
        }

        public void setOriginalVerified(String originalVerified) {
            this.originalVerified = originalVerified;
        }

        // ORIGINAL Followers Count
        public String getOriginalFollowersCount() {
            return originalFollowersCount;
        }

        public void setOriginalFollowersCount(String originalFollowersCount) {
            this.originalFollowersCount = originalFollowersCount;
        }

        // ORIGINAL Following Count
        public String getOriginalFollowingCount() {
            return originalFollowingCount;
        }

        public void setOriginalFollowingCount(String originalFollowingCount) {
            this.originalFollowingCount = originalFollowingCount;
        }
    }

    public static class GroupOfReactions {
        @JsonProperty("react_id")
        private String reactId;

        @JsonProperty("reaction_group_of_authors_id")
        private String reactionGroupOfAuthorsId;

        @JsonProperty("nr_of_distinct_authors")
        private String nrOfDistinctAuthors;

        @JsonProperty("reaction_group_of_tweets_id")
        private String reactionGroupOfTweetsId;

        @JsonProperty("time_interval")
        private String timeInterval;

        @JsonProperty("nr_of_reactions")
        private String nrOfReactions;

        @JsonProperty("percentage_out_of_total_reactions")
        private String percentageOutOfTotalReactions;

        @JsonProperty("nr_of_replies")
        private String nrOfReplies;

        @JsonProperty("nr_of_quotes")
        private String nrOfQuotes;

        @JsonProperty("nr_of_retweets")
        private String nrOfRetweets;

        // Add getters and setters
        // React ID
        public String getReactId() {
            return reactId;
        }

        public void setReactId(String reactId) {
            this.reactId = reactId;
        }

        // Reaction Group of Authors ID
        public String getReactionGroupOfAuthorsId() {
            return reactionGroupOfAuthorsId;
        }

        public void setReactionGroupOfAuthorsId(String reactionGroupOfAuthorsId) {
            this.reactionGroupOfAuthorsId = reactionGroupOfAuthorsId;
        }

        // Number of Distinct Authors
        public String getNrOfDistinctAuthors() {
            return nrOfDistinctAuthors;
        }

        public void setNrOfDistinctAuthors(String nrOfDistinctAuthors) {
            this.nrOfDistinctAuthors = nrOfDistinctAuthors;
        }

        // Reaction Group of Tweets ID
        public String getReactionGroupOfTweetsId() {
            return reactionGroupOfTweetsId;
        }

        public void setReactionGroupOfTweetsId(String reactionGroupOfTweetsId) {
            this.reactionGroupOfTweetsId = reactionGroupOfTweetsId;
        }

        // Time Interval
        public String getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(String timeInterval) {
            this.timeInterval = timeInterval;
        }

        // Number of Total Reactions
        public String getNrOfReactions() {
            return nrOfReactions;
        }

        public void setNrOfReactions(String nrOfReactions) {
            this.nrOfReactions = nrOfReactions;
        }

        // Percentage of Total Reactions
        public String getPercentageOutOfTotalReactions() {
            return percentageOutOfTotalReactions;
        }

        public void setPercentageOutOfTotalReactions(String percentageOutOfTotalReactions) {
            this.percentageOutOfTotalReactions = percentageOutOfTotalReactions;
        }

        // Number of Replies
        public String getNrOfReplies() {
            return nrOfReplies;
        }

        public void setNrOfReplies(String nrOfReplies) {
            this.nrOfReplies = nrOfReplies;
        }

        // Number of Quotes
        public String getNrOfQuotes() {
            return nrOfQuotes;
        }

        public void setNrOfQuotes(String nrOfQuotes) {
            this.nrOfQuotes = nrOfQuotes;
        }

        // Number of Retweets
        public String getNrOfRetweets() {
            return nrOfRetweets;
        }

        public void setNrOfRetweets(String nrOfRetweets) {
            this.nrOfRetweets = nrOfRetweets;
        }
    }

}
