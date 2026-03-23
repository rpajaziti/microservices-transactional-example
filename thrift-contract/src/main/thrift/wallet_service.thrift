namespace java com.example.thrift.wallet

exception TWalletException {
    1: i32 code,
    2: string message
}

service TWalletService {
    void deductBalance(1: string userId, 2: i32 amount) throws (1: TWalletException e)
    void topUp(1: string userId, 2: i32 amount) throws (1: TWalletException e)
}
