/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class CompetitionAsset {

    @Property()
    private final String did;

    @Property()
    private final String name;

    @Property()
    private final String competitionName;

    @Property()
    private final String achievement;

    @Property()
    private final String organizer;

    @Property()
    private final String summary;

    @Property()
    private final String term;

    @Property()
    private final String type;

    public String getDid() {
        return did;
    }

    public String getName() {
        return name;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getSummary() {
        return summary;
    }

    public String getTerm() {
        return term;
    }

    public String getType() {
        return type;
    }

    public CompetitionAsset(@JsonProperty("did") final String did, @JsonProperty("name") final String name,
                            @JsonProperty("competitionName") final String competitionName, @JsonProperty("achievement") final String achievement,
                            @JsonProperty("organizer") final String organizer, @JsonProperty("summary") final String summary,
                            @JsonProperty("term") final String term) {
        this.did = did;
        this.name = name;
        this.competitionName = competitionName;
        this.achievement = achievement;
        this.organizer = organizer;
        this.summary = summary;
        this.term = term;
        this.type = "CompetitionAsset";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        CompetitionAsset other = (CompetitionAsset) obj;

        return Objects.deepEquals(
                new String[]{getDid(), getName(), getCompetitionName(), getAchievement(), getOrganizer(), getSummary(), getTerm()},
                new String[]{other.getDid(), other.getName(), other.getCompetitionName(), other.getAchievement(), other.getOrganizer(),
                        other.getSummary(), other.getTerm()});

    }

    @Override
    public int hashCode() {
        return Objects.hash(getDid(), getName(), getCompetitionName(), getAchievement(), getOrganizer(), getSummary(), getTerm());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [did=" + did + ", name="
                + name + ", competitionName=" + competitionName + ", achievement=" + achievement + ", organizer=" + organizer
                + ", summary=" + summary + ", term=" + term + ", type=" + type + "]";

    }
}
