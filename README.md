# ethereum-android (geth)

## 0 Setup envirement
In your app build.gradle, add the line below
```
compile 'org.ethereum:geth:1.7.1'
```
## 1 KeyStore and Transaction
### 1.1 Create KeyStore
 ```
 KeyStore ks = new KeyStore(Environment.getExternalStorageDirectory().getPath() + 
 "/keystore", 
 Geth.LightScryptN, 
 Geth.LightScryptP);
```
### 1.2 Create Account
```
String passwd = "123456";
Account act = ks.newAccount(passwd);
```
### 1.3 Connect to a ethereum node
```
EthereumClient ec = null;
ec = Geth.newEthereumClient("http://127.0.0.1:8545");//Your ethereum address
```
### 1.4 Create a context
```
Context ctx = Geth.newContext();//Create a context
```
### 1.5 Create a transaction instance
```
Transaction tx = null;
tx = Geth.newTransaction(ec.getNonceAt(ctx, act.getAddress(), -1)/*nonce*/, 
new Address("0x218FeeF49FB0582c7bB739ab0DEf617c651ec8c3")/*to your address*/,
new BigInt(10000000)/*ether*/, 
new BigInt(4300000)/*gas limit*/, 
new BigInt(300000)/*gas price*/, 
null);
```
### 1.6 Sign a transaction
```
Transaction signedtx = null;
signedtx = ks.signTxPassphrase(act, "123456", tx, new BigInt(13539919)/*Your network ID*/);
```
### 1.7 Send a transaction
```
ec.sendTransaction(ctx,signedtx);
```
## 1.8 Get a receipt
```
Receipt rec = null;
rec = ec.getTransactionReceipt(ctt, signedtx.getHash());
```
## 2 Contract
### 2.1 Prepare deploy a contract
```
TransactOpts tops = new TransactOpts();
tops.setContext(ctx);
tops.setFrom(act.getAddress());
tops.setGasLimit(900000);
tops.setGasPrice(new BigInt(30000));
tops.setSigner(new MySigner(act,ks,"123456", new BigInt(13539919)));//Refer MySigner.java
tops.setNonce(ec.getNonceAt(ctt, act.getAddress(), -1));
```
### 2.2 Put the code and abi of your contract here
```
String bytecode = "6060604052341561000c57fe5b5b60b38061001b6000396000f300606060405263ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631003e2d2811460435780636d4ce63c146055575bfe5b3415604a57fe5b60536004356074565b005b3415605c57fe5b60626080565b60408051918252519081900360200190f35b60008054820190555b50565b6000545b905600a165627a7a72305820cea55ffbb44b744ad40c6f202f52d1fcd2d8cc0a1cf29b6b3f93e6a4b1b0f3120029";
String abi = "[{\"constant\":false,\"inputs\":[{\"name\":\"i\",\"type\":\"uint256\"}],\"name\":\"add\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"c\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";
```
### 2.3 Deploy the contract now
```
Geth.deployContract(tops, abi, hexStringToByteArray(bytecode), ec, new Interfaces(0));
```
### 2.4 Get value from the contract
```
CallMsg callMsg = Geth.newCallMsg();
callMsg.setTo(new Address("218FeeF49FB0582c7bB739ab0DEf617c651ec8c3".toLowerCase()));//This is the contract address
callMsg.setData(hexStringToByteArray("6d4ce63c"));
byte[] ret = null;
ret = ec.callContract(ctt, callMsg, -1);
Log.e("ret is ", bytesToHex(ret));
```
### 2.5 Set the value to the contract
You need send transaction, and you need set data

Enjoy it!

# Appendix
#### 1 Refer function hex string->bytearray and bytearray->hex string
```
    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public final static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
```
