/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(
        name = "basic",
        info = @Info(
                title = "Asset Transfer",
                description = "The hyperlegendary asset transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class AssetTransfer implements ContractInterface {

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    // Initialization method
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreateUnivClassAsset(ctx, "did1", "김서연", "202146147", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateUnivClassAsset(ctx, "did2", "남준성", "202146739", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateUnivClassAsset(ctx, "did3", "손영빈", "201917708", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateUnivClassAsset(ctx, "did4", "이용준", "201917716", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateUnivClassAsset(ctx, "did5", "한은규", "201911114", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateUnivClassAsset(ctx, "did6", "홍길동", "202312345", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤");
        CreateCompetitionAsset(ctx, "did7", "유재석", "아이디어해커톤", "1st place", "전북대학교 소프트웨어공학과", "블록체인기반폴관리서비스");
        CreateCompetitionAsset(ctx, "did8", "박명수", "아이디어해커톤", "2nd place", "전북대학교 소프트웨어공학과", "의류관리서비스");
    }

    // UnivClassAsset methods
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public UnivClassAsset CreateUnivClassAsset(final Context ctx, final String did, final String name, final String studentID, final String term, final String summary, final String subject) {
        ChaincodeStub stub = ctx.getStub();

        if (AssetExists(ctx, did)) {
            String errorMessage = String.format("Asset %s already exists", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        UnivClassAsset univClassAsset = new UnivClassAsset(did, name, studentID, term, summary, subject);
        String sortedJson = genson.serialize(univClassAsset);
        stub.putStringState(did, sortedJson);

        return univClassAsset;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public UnivClassAsset ReadUnivClassAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        UnivClassAsset univClassAsset = genson.deserialize(assetJSON, UnivClassAsset.class);
        return univClassAsset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public UnivClassAsset UpdateUnivClassAsset(final Context ctx, final String did, final String name, final String studentID, final String term, final String summary, final String subject) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, did)) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        UnivClassAsset univClassAsset = new UnivClassAsset(did, name, studentID, term, summary, subject);
        String sortedJson = genson.serialize(univClassAsset);
        stub.putStringState(did, sortedJson);
        return univClassAsset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteUnivClassAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, did)) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(did);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String TransferUnivClassAsset(final Context ctx, final String did, final String newTerm) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        UnivClassAsset univClassAsset = genson.deserialize(assetJSON, UnivClassAsset.class);

        UnivClassAsset newUnivClassAsset = new UnivClassAsset(univClassAsset.getDid(), univClassAsset.getName(), univClassAsset.getStudentID(), newTerm, univClassAsset.getSummary(),
                univClassAsset.getSubject());
        String sortedJson = genson.serialize(newUnivClassAsset);
        stub.putStringState(did, sortedJson);

        return univClassAsset.getTerm();
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllUnivClassAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<UnivClassAsset> queryResults = new ArrayList<UnivClassAsset>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            UnivClassAsset univClassAsset = genson.deserialize(result.getStringValue(), UnivClassAsset.class);
            System.out.println(univClassAsset);
            queryResults.add(univClassAsset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    // CompetitionAsset methods
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public CompetitionAsset CreateCompetitionAsset(final Context ctx, final String did, final String name, final String competitionName, final String achievement, final String organizer, final String summary) {
        ChaincodeStub stub = ctx.getStub();

        if (CompetitionAssetExists(ctx, did)) {
            String errorMessage = String.format("CompetitionAsset %s already exists", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        CompetitionAsset competitionAsset = new CompetitionAsset(did, name, competitionName, achievement, organizer, summary);
        String sortedJson = genson.serialize(competitionAsset);
        stub.putStringState(did, sortedJson);

        return competitionAsset;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public CompetitionAsset ReadCompetitionAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("CompetitionAsset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        CompetitionAsset competitionAsset = genson.deserialize(assetJSON, CompetitionAsset.class);
        return competitionAsset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public CompetitionAsset UpdateCompetitionAsset(final Context ctx, final String did, final String name, final String competitionName, final String achievement, final String organizer, final String summary) {
        ChaincodeStub stub = ctx.getStub();

        if (!CompetitionAssetExists(ctx, did)) {
            String errorMessage = String.format("CompetitionAsset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        CompetitionAsset competitionAsset = new CompetitionAsset(did, name, competitionName, achievement, organizer, summary);
        String sortedJson = genson.serialize(competitionAsset);
        stub.putStringState(did, sortedJson);
        return competitionAsset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteCompetitionAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();

        if (!CompetitionAssetExists(ctx, did)) {
            String errorMessage = String.format("CompetitionAsset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(did);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean CompetitionAssetExists(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllCompetitionAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<CompetitionAsset> queryResults = new ArrayList<CompetitionAsset>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            CompetitionAsset competitionAsset = genson.deserialize(result.getStringValue(), CompetitionAsset.class);
            System.out.println(competitionAsset);
            queryResults.add(competitionAsset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    // Combined method to get all entries
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllEntries(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Object> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            String json = result.getStringValue();
            if (json.contains("\"type\":\"CompetitionAsset\"")) {
                CompetitionAsset competitionAsset = genson.deserialize(json, CompetitionAsset.class);
                queryResults.add(competitionAsset);
            } else {
                UnivClassAsset univClassAsset = genson.deserialize(json, UnivClassAsset.class);
                queryResults.add(univClassAsset);
            }
        }
        final String response = genson.serialize(queryResults);
        return response;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        return (assetJSON != null && !assetJSON.isEmpty());
    }
}
