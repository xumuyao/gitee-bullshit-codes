package com.gitee.bullshit.code;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

public class PreAfterList {

    // 需求：根据 ID 获取指定 ID 文章的包括当前 ID 及上一条和下一条内容集合
    // 原样贴出天才同事的操作如下，看完肺都气炸了！！ 看官们你觉得正确做法是什么？？
    public ArticleVO getArticleVO(Long id) {
        ArticleVO vo = new ArticleVO(getById(id));
        List<Article> list = articleMapper.selectList(baseMapper.selectList(Wrappers.<Article>lambdaQuery().orderByDesc(Article::getCreateTime)));
        if (list.size() == 1) {
            return vo;
        } else if (list.size() == 2) {
            if (list.get(0).getId().equals(vo.getId())) {
                // first is itself
                vo.setAfter(list.get(1));
            } else {
                vo.setPre(list.get(0));
            }
        } else {
            // > 3
            Article pre = list.get(0);
            if (pre.getId().equals(vo.getId())) {
                vo.setPre(null);
                vo.setAfter(list.get(1));
                return vo;
            }
            for (Article article : list) {
                if (article.getId().equals(vo.getId())) {
                    vo.setPre(pre);
                    int preIdx = list.indexOf(pre);
                    if (preIdx + 2 >= list.size()) {
                        break;
                    }
                    vo.setAfter(list.get(preIdx + 2));
                    break;
                } else {
                    pre = article;
                }
            }
        }
        return vo;
    }
}
