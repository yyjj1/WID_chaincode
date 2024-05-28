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

    // Existing methods for Asset

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreateAsset(ctx, new Asset("did1", "김서연", "202146147", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateAsset(ctx, new Asset("did2", "남준성", "202146739", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateAsset(ctx, new Asset("did3", "손영빈", "201917708", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateAsset(ctx, new Asset("did4", "이용준", "201917716", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateAsset(ctx, new Asset("did5", "한은규", "201911114", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateAsset(ctx, new Asset("did6", "홍길동", "202312345", "2024/03/02~2024/06/21", "블록체인기반포폴관리", "캡스톤", "김순태"));
        CreateCompetitionAsset(ctx, new CompetitionAsset("did7", "유재석", "아이디어해커톤", "1st place", "전북대학교 소프트웨어공학과", "블록체인기반폴관리서비스", "2024/05/05"));
        CreateCompetitionAsset(ctx, new CompetitionAsset("did8", "박명수", "아이디어해커톤", "2nd place", "전북대학교 소프트웨어공학과", "의류관리서비스", "2024/05/05"));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Asset CreateAsset(final Context ctx, final Asset asset) {
        ChaincodeStub stub = ctx.getStub();

        if (AssetExists(ctx, asset.getDid())) {
            String errorMessage = String.format("Asset %s already exists", asset.getDid());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        String sortedJson = genson.serialize(asset);
        stub.putStringState(asset.getDid(), sortedJson);

        return asset;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Asset ReadAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset asset = genson.deserialize(assetJSON, Asset.class);
        return asset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Asset UpdateAsset(final Context ctx, final Asset asset) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, asset.getDid())) {
            String errorMessage = String.format("Asset %s does not exist", asset.getDid());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        String sortedJson = genson.serialize(asset);
        stub.putStringState(asset.getDid(), sortedJson);
        return asset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, did)) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(did);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String did) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String TransferAsset(final Context ctx, final String did, final String newTerm) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(did);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", did);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset asset = genson.deserialize(assetJSON, Asset.class);

        Asset newAsset = new Asset(asset.getDid(), asset.getName(), asset.getStudentID(), newTerm, asset.getSummary(),
                asset.getSubject(), asset.getProfessor());
        String sortedJson = genson.serialize(newAsset);
        stub.putStringState(did, sortedJson);

        return asset.getTerm();
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Asset> queryResults = new ArrayList<Asset>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Asset asset = genson.deserialize(result.getStringValue(), Asset.class);
            System.out.println(asset);
            queryResults.add(asset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    // New methods for CompetitionAsset

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public CompetitionAsset CreateCompetitionAsset(final Context ctx, final CompetitionAsset competitionAsset) {
        ChaincodeStub stub = ctx.getStub();

        if (CompetitionAssetExists(ctx, competitionAsset.getDid())) {
            String errorMessage = String.format("CompetitionAsset %s already exists", competitionAsset.getDid());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        String sortedJson = genson.serialize(competitionAsset);
        stub.putStringState(competitionAsset.getDid(), sortedJson);

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
    public CompetitionAsset UpdateCompetitionAsset(final Context ctx, final CompetitionAsset competitionAsset) {
        ChaincodeStub stub = ctx.getStub();

        if (!CompetitionAssetExists(ctx, competitionAsset.getDid())) {
            String errorMessage = String.format("CompetitionAsset %s does not exist", competitionAsset.getDid());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        String sortedJson = genson.serialize(competitionAsset);
        stub.putStringState(competitionAsset.getDid(), sortedJson);
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
}
