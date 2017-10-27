package com.example.mobilerover.testgeth;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Signer;
import org.ethereum.geth.TransactOpts;
import org.ethereum.geth.Transaction;

import java.security.Key;

/**
 * Created by mobilerover on 2017/10/27.
 */

public class MySigner implements Signer {

    private KeyStore keyStore = null;
    private String passwd = null;
    private BigInt chainId = null;
    private Account account = null;

    @Override
    public Transaction sign(Address address, Transaction transaction) throws Exception {

        Transaction tx = null;
        tx = keyStore.signTxPassphrase(account, passwd, transaction, chainId);

        return tx;
    }

    public MySigner(Account account, KeyStore ks, String passwd, BigInt chainId) {

        this.keyStore = ks;
        this.passwd = passwd;
        this.chainId = chainId;
        this.account = account;
    }
}
