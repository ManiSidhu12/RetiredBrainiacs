package com.retiredbrainiacs.model.store;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreProductsRoot {

@SerializedName("status")
@Expose
private String status;
@SerializedName("list_products")
@Expose
private List<ListProduct> listProducts = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<ListProduct> getListProducts() {
return listProducts;
}

public void setListProducts(List<ListProduct> listProducts) {
this.listProducts = listProducts;
}

}