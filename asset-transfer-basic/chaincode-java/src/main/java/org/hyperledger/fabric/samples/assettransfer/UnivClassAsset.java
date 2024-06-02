/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class UnivClassAsset {

    @Property()
    private final String did;

    @Property()
    private final String name;

    @Property()
    private final String studentID;

    @Property()
    private final String term;

    @Property()
    private final String summary;

    @Property()
    private final String subject;

//    @Property()
//    private final String professor;

    @Property()
    private final String type;

    public String getDid() {
        return did;
    }

    public String getName() {
        return name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getTerm() {
        return term;
    }

    public String getSummary() {
        return summary;
    }

    public String getSubject() {
        return subject;
    }

//    public String getProfessor() {
//        return professor;
//    }

    public String getType() {
        return type;
    }

    public UnivClassAsset(@JsonProperty("did") final String did, @JsonProperty("name") final String name,
                          @JsonProperty("studentID") final String studentID, @JsonProperty("term") final String term,
                          @JsonProperty("summary") final String summary, @JsonProperty("subject") final String subject) {
        this.did = did;
        this.name = name;
        this.studentID = studentID;
        this.term = term;
        this.summary = summary;
        this.subject = subject;
//        this.professor = professor;
        this.type = "UnivClassAsset";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        UnivClassAsset other = (UnivClassAsset) obj;

        return Objects.deepEquals(
                new String[] {getDid(), getName(), getTerm(), getSubject(), getStudentID(), getSummary()},
                new String[] {other.getDid(), other.getName(), other.getTerm(), other.getSubject(), other.getStudentID(),
                        other.getSummary()});

    }

    @Override
    public int hashCode() {
        return Objects.hash(getDid(), getName(), getStudentID(), getTerm(), getSummary(), getSubject());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [did=" + did + ", name="
                + name + ", studentID=" + studentID + ", term=" + term + ", summary=" + summary
                + ", subject=" + subject + ", type=" + type + "]";

    }
}
