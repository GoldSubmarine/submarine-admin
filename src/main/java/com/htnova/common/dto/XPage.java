package com.htnova.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htnova.common.base.BaseMapStruct;
import java.util.List;
import org.mapstruct.factory.Mappers;

public interface XPage<T> {
    long getPageSize();

    void setPageSize(long size);

    long getPageNum();

    void setPageNum(long num);

    long getTotal();

    void setTotal(long total);

    List<OrderItem> getOrders();

    void setOrders(List<OrderItem> orderItemList);

    List<T> getData();

    void setData(List<T> orderItemList);

    static <V, T> IPage<V> toIPage(XPage<T> xPage) {
        Page<V> page = new Page<>();
        page.setSize(xPage.getPageSize());
        page.setCurrent(xPage.getPageNum());
        page.setOrders(xPage.getOrders());
        return page;
    }

    static <T, V> XPage<T> fromIPage(IPage<V> iPage, Class<? extends BaseMapStruct<T, V>> mapStruct) {
        XPageImpl<T> xPage = new XPageImpl<>();
        xPage.setPageNum(iPage.getCurrent());
        xPage.setPageSize(iPage.getSize());
        xPage.setTotal(iPage.getTotal());
        xPage.setOrders(iPage.orders());

        BaseMapStruct<T, V> mapper = Mappers.getMapper(mapStruct);
        xPage.setData(mapper.toDto(iPage.getRecords()));
        return xPage;
    }

    @JsonIgnore
    default long getStartIndex() {
        return (this.getPageNum() - 1) * this.getPageSize();
    }

    @JsonIgnore
    default long getEndIndex() {
        return this.getPageNum() * this.getPageSize();
    }
}
