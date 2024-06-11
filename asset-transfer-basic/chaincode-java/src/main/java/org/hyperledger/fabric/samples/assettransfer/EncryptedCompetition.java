package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class EncryptedCompetition {

    @Property()
    private final String did;

    @Property()
    private final String encryptedcompetition;

    @Property()
    private final String addedcompetition;

    @Property()
    private final String type;

    public String getDid() {
        return did;
    }

    public String getEncryptedcompetition() {
        return encryptedcompetition;
    }

    public String getAddedcompetition() {
        return addedcompetition;
    }

    public String getType() {
        return type;
    }

    public EncryptedCompetition(@JsonProperty("did") final String did, @JsonProperty("encryptedcompetition") final String encryptedcompetition,
                            @JsonProperty("addedcompetition") final String addedcompetition) {
        this.did = did;
        this.encryptedcompetition = encryptedcompetition;
        this.addedcompetition = addedcompetition;
        this.type = "EncryptedCompetition";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        EncryptedCompetition other = (EncryptedCompetition) obj;

        return Objects.deepEquals(
                new String[]{getDid(), getEncryptedcompetition(), getAddedcompetition()},
                new String[]{other.getDid(), other.getEncryptedcompetition(), other.getAddedcompetition()});
    }



    @Override
    public int hashCode() {
        return Objects.hash(getDid(), getEncryptedcompetition(), getAddedcompetition());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [did=" + did
                + ", encryptedcompetition=" + encryptedcompetition + ", addedcompetition=" + addedcompetition
                + ", type=" + type + "]";

    }

}
