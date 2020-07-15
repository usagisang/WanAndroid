package com.gochiusa.wanandroid.util;

/**
 *  辅助计算偏移量的工具类
 */
public final class OffsetCalculator {
    private int offset;
    private int pageLimit;
    private int totalCount;

    public int getOffset() {
        return offset;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public OffsetCalculator(int offset, int pageLimit, int totalCount) {
        this.offset = offset;
        this.totalCount = totalCount;
        this.pageLimit = pageLimit;
    }


    /**
     *  使偏移量递增
     * @return 偏移量到达最大值，返回false，表示递增失败，否则返回true代表偏移量递增成功
     */
    public boolean increaseOffset() {
        offset += pageLimit;
        if (offset > totalCount) {
            offset = totalCount;
            return false;
        } else {
            return true;
        }
    }

    /**
     *  获取当前的页码数
     */
    public int getPage() {
        return offset / pageLimit;
    }

}
