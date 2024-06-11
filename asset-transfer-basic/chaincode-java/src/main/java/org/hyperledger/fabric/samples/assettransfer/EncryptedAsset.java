package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class EncryptedAsset {
    @Property()
    private final String did;

    @Property()
    private final String encryptedstring;

    @Property()
    private final String addedstring;

    @Property()
    private final String type;

    public String getDid() {
        return did;
    }

    public String getEncryptedstring() {
        return encryptedstring;
    }

    public String getAddedstring() {
        return addedstring;
    }

    public String getType() {
        return type;
    }

    public EncryptedAsset(@JsonProperty("did") final String did, @JsonProperty("encryptedstring") final String encryptedstring,
                          @JsonProperty("addedstring") final String addedstring, @JsonProperty("type") final String type) {
        this.did = did;
        this.encryptedstring = encryptedstring;
        this.addedstring = addedstring;
        this.type = type;
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
                new String[]{getDid(), getEncryptedstring(), getAddedstring(), getType()},
                new String[]{other.getDid(), other.getEncryptedcompetition(), other.getAddedcompetition(), other.getType()});
    }



    @Override
    public int hashCode() {
        return Objects.hash(getDid(), getEncryptedstring(), getAddedstring());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [did=" + did
                + ", encryptedstring=" + encryptedstring + ", addedstring=" + addedstring
                + ", type=" + type + "]";

    }
}
