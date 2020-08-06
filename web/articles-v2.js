window.addEventListener("load", function () {
    // TODO Could extract BASE_URL into the header of the document to reduce the amount of code duplication
    const BASE_URL = `/ajax/v2/`;

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

    // async function getFullArticleObj(articleId) {
    //     let articleResponseObj = await fetch(`${BASE_URL}articles?id=${articleId}`);
    //     let articleJson = await articleResponseObj.json();
    //     return articleJson;
    // }

    async function getUserObj(userId) {
        let userResponseObj = await fetch(`${BASE_URL}users?id=${userId}`);
        let userJsonObj = await userResponseObj.json();
        return userJsonObj;
    }


    //Have changed the fetch to access userV2 to show it's slightly different to versions 0 and 1.
    // async function getLikesArray(userId) {
    //     let userResponseObj = await fetch(`${BASE_URL}likes?userV2=${userId}`);
    //     let userJsonObj = await userResponseObj.json();
    //     return userJsonObj;
    // }

    async function displayNextArticlesOnPage() {
        document.querySelector('#article-load-button').removeEventListener("click", displayNextArticlesOnPage);

        let articlesJsonArray = await getArticleArray(loadArticleNext, loadArticleCount);
        for (let i = 0; i < articlesJsonArray.length; i++) {
            //Removed authorObj from function call below to fit the new displayPartialArticleOnPage function
            displayPartialArticleOnPage(articlesJsonArray[i]);
        }
        if (articlesJsonArray.length < loadArticleCount) {
            document.querySelector('#article-load-button').style.background = "red";
        } else {
            document.querySelector('#article-load-button').addEventListener("click", displayNextArticlesOnPage);
            loadArticleNext += loadArticleCount;
        }
    }

    //New revised displayPartialArticleOnPage function so that a authorObj is not required - as it's been added to the article
    function displayPartialArticleOnPage(articleObj) {
        let articleDivElement = document.createElement("div");
        articleDivElement.classList.add("article");
        //we'll cut down the blurb here so that the content is still accessible later
        let articleBlurb = articleObj.content.substring(0,50);
        articleDivElement.innerHTML = `
                <h3 class="article-title">${articleObj.title}</h3>
                <h4 class="article-author" data-author-id="${articleObj.author_id}">${articleObj.author_fname} ${articleObj.author_lname}</h4>
                <p class="article-body" data-article-content ="${articleObj.content}"> ${articleBlurb}</p>
                <div class="article-read-more button" data-article-id="${articleObj.id}">Show full content</div>`;
        let articlesDiv = document.querySelector("#articles-inner");
        articlesDiv.appendChild(articleDivElement);

        articleDivElement.querySelector('.article-author').addEventListener('click', displayAuthorDetailsOnPage);
        articleDivElement.querySelector('.article-read-more').addEventListener('click', displayFullArticleOnPage);
    }

    async function displayFullArticleOnPage(event) {
        let articleContent = event.target.previousElementSibling.getAttribute("data-article-content");
        let articleContentDiv = event.target.previousElementSibling;
        articleContentDiv.innerText = articleContent;

        //Below we'll remove the custom attribute data-article-content to an empty string - to save on memory as it's no longer required
        articleContentDiv.removeAttribute("data-article-content");
        event.target.remove();
    }

    async function displayAuthorDetailsOnPage(event) {
        let authorId = event.target.getAttribute("data-author-id");
        let authorObj = await getUserObj(authorId);

        document.querySelector("#user-details-first-name").innerText = authorObj.fname;
        document.querySelector("#user-details-last-name").innerText = authorObj.lname;
        document.querySelector("#user-details-gender").innerText = authorObj.gender;

        let likedList = document.querySelector("#user-details-liked-articles");
        likedList.innerHTML = "";
        console.log(authorObj);
        for (let i = 0; i < authorObj.likedArticles.length; i++) {

            let listItem = document.createElement("li");
            listItem.innerText = authorObj.likedArticles[i].title;
            likedList.appendChild(listItem);
        }
    }

});