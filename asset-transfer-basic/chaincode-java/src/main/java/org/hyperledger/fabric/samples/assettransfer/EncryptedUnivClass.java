package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class EncryptedUnivClass {

    @Property()
    private final String did;

    @Property()
    private final String encryptedunivclass;

    @Property()
    private final String addedunivclass;

    @Property()
    private final String type;

    public String getDid() {
        return did;
    }

    public String getEncryptedunivclass() {
        return encryptedunivclass;
    }

    public String getAddedunivclass() {

        return addedunivclass;
    }

    public String getType() {

        return type;
    }

    public EncryptedUnivClass(@JsonProperty("did") final String did, @JsonProperty("encryptedunivclass") final String encryptedunivclass,
                          @JsonProperty("addedunivclass") final String addedunivclass) {
        this.did = did;
        this.encryptedunivclass = encryptedunivclass;
        this.addedunivclass = addedunivclass;
        this.type = "EncryptedUnivClass";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        EncryptedUnivClass other = (EncryptedUnivClass) obj;

        return Objects.deepEquals(
                new String[] {getDid(), getEncryptedunivclass(), getAddedunivclass()},
                new String[] {other.getDid(), other.getEncryptedunivclass(), other.getAddedunivclass()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDid(), getEncryptedunivclass(), getAddedunivclass());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [did=" + did
                + ", encryptedunivclass=" + encryptedunivclass + ", addedunivclass=" + addedunivclass
                + ", type=" + type + "]";

    }
}
