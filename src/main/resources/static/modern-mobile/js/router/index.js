import HomePage from "../pages/home.js";
import CatalogPage from "../pages/catalog.js";
import ReaderPage from "../pages/reader.js";
import FileListPage from "../pages/file-list.js";
import ProfilePage from "../pages/profile.js";

const routes = [
    { path: "/", component: HomePage, meta: { title: "书城" } },
    { path: "/catalog/:bookid", component: CatalogPage, meta: { title: "目录" } },
    { path: "/reader/:id", component: ReaderPage, meta: { title: "阅读" } },
    { path: "/files/:pathMatch(.*)*", component: FileListPage, meta: { title: "文件" } },
    { path: "/me", component: ProfilePage, meta: { title: "我的" } }
];

export default window.VueRouter.createRouter({
    history: window.VueRouter.createWebHashHistory(),
    routes
});
