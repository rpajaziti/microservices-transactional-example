namespace java com.example.thrift.inventory

exception TInventoryException {
    1: i32 code,
    2: string message
}

struct TProduct {
    1: i64 id,
    2: string productName,
    3: i32 stock
}

service TInventoryService {
    void decrementStock(1: i64 productId, 2: i32 quantity) throws (1: TInventoryException e)
    list<TProduct> listProducts()
}
