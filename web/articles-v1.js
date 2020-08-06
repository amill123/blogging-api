window.addEventListener("load", function () {
    // TODO Could extract BASE_URL into the header of the document to reduce the amount of code duplication
    const BASE_URL = `/ajax/v1/`;

    /* Variables to control where the application is with loading articles and how many it should load at a time */
    const loadArticleCount = 2;
    //Changed loadArticleNext to 1 (from 0), as SQL stores the first article at row #1.
    let loadArticleNext = 1;

    displayNextArticlesOnPage();
    document.querySelector('#article-load-button').addEventListener("click", displayNextArticlesOnPage);

    async function getArticleArray(from, count) {

        let articlesResponseObj = await fetch(`${BASE_URL}articles?from=${from}&count=${count}`);
        let articlesJsonArray = await articlesResponseObj.json();
        console.log(articlesJsonArray);
        return articlesJsonArray;
    }

    async function getFullArticleObj(articleId) {
        let articleResponseObj = await fetch(`${BASE_URL}articles?id=${articleId}`);
        let articleJson = await articleResponseObj.json();
        return articleJson;
    }

    async function getUserObj(userId) {
        let userResponseObj = await fetch(`${BASE_URL}users?id=${userId}`);
        let userJsonObj = await userResponseObj.json();
        return userJsonObj;
    }

    async function getLikesArray(userId) {
        let userResponseObj = await fetch(`${BASE_URL}likes?user=${userId}`);
        let userJsonObj = await userResponseObj.json();
        return userJsonObj;
    }

    async function displayNextArticlesOnPage() {
        document.querySelector('#article-load-button').removeEventListener("click", displayNextArticlesOnPage);
        let articlesJsonArray = await getArticleArray(loadArticleNext, loadArticleCount);

        for (let i = 0; i < articlesJsonArray.length; i++) {
            //This fetch has actually been made redundant as a modification to the ArticlesDAO returns the first and last names of the author with the article.
            //However, for effect this has been left in the code to show the delay
            let authorObj = await getUserObj(articlesJsonArray[i].author_id);
            displayPartialArticleOnPage(articlesJsonArray[i], authorObj);
        }

        if (articlesJsonArray.length < loadArticleCount) {
            document.querySelector('#article-load-button').style.background = "red";
        } else {
            document.querySelector('#article-load-button').addEventListener("click", displayNextArticlesOnPage);
            loadArticleNext += loadArticleCount;
        }
    }

    function displayPartialArticleOnPage(articleObj, authorObj) {
        let articleDivElement = document.createElement("div");
        articleDivElement.classList.add("article");

        //Insert blurb - moving this from server-side logic to make the servlet/DAO work with Version 2
        let blurb = articleObj.content.substring(0,50);
        articleDivElement.innerHTML = `
                <h3 class="article-title">${articleObj.title}</h3>
                <h4 class="article-author" data-author-id="${authorObj.id}">${authorObj.fname} ${authorObj.lname}</h4>
                <p class="article-body">${blurb}</p>
                <div class="article-read-more button" data-article-id="${articleObj.id}">Show full content</div>`;

        let articlesDiv = document.querySelector("#articles-inner");
        articlesDiv.appendChild(articleDivElement);

        articleDivElement.querySelector('.article-author').addEventListener('click', displayAuthorDetailsOnPage);
        articleDivElement.querySelector('.article-read-more').addEventListener('click', displayFullArticleOnPage);
    }

    async function displayFullArticleOnPage(event) {
        let articleId = event.target.getAttribute("data-article-id");
        let fullArticleObj = await getFullArticleObj(articleId);
        let articleContentDiv = event.target.previousElementSibling;
        articleContentDiv.innerText = fullArticleObj.content;
        event.target.remove();
    }

    async function displayAuthorDetailsOnPage(event) {
        let authorId = event.target.getAttribute("data-author-id");
        let authorObj = await getUserObj(authorId);

        document.querySelector("#user-details-first-name").innerText = authorObj.fname;
        document.querySelector("#user-details-last-name").innerText = authorObj.lname;
        document.querySelector("#user-details-gender").innerText = authorObj.gender;

        let authorLikesArray = await getLikesArray(authorObj.id);
        let likedList = document.querySelector("#user-details-liked-articles");
        likedList.innerHTML = "";

        for (let i = 0; i < authorLikesArray.length; i++) {
            let articleObj = await getFullArticleObj(authorLikesArray[i]);

            let listItem = document.createElement("li");
            listItem.innerText = articleObj.title;
            likedList.appendChild(listItem);
        }
    }

});