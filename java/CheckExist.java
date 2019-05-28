/**
 * 是否收藏(有没有感觉这段代码特别特别啰嗦，不够简洁）
 * @param articleKey
 * @param user
 * @return
 */
public boolean isFavorite(String articleKey, User user) {
    boolean isFavorite = false;
    if (user == null) {
        return false;
    }
    long userId = user.getId();
    Article article = getByArticleKey(articleKey);
    if (null == article) {
        return false;
    }
    List<Long> list = listAttentionUsers(article);
    if (list == null || list.size() <= 0) {
        return false;
    }
    for (Long id : list) {
        if (id == userId) {
            isFavorite = true;
            break;
        }
    }
    return isFavorite;
}
