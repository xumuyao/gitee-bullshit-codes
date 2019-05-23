class CatchDemo {

    // 异常捕获，什么事都不做
    static {
        try {
            gun = Resources.parseModel("images/gun.txt");
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}