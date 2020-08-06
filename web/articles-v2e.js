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
        return articlesJsonArray;
    }


    async function getUserObj(userId) {
        let userResponseObj = await fetch(`${BASE_URL}users?id=${userId}`);
        let userJsonObj = await userResponseObj.json();
        return userJsonObj;
    }


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

        //Now we'll create the list of comments
        let list = document.createElement('ul');
        let commentHeading = document.createElement('h4');
        commentHeading.innerHTML = 'Comments';
        list.appendChild(commentHeading);
        //If there are no comments we'll say No comments found.
        if(articleObj.comments.length === 0){
            let listItem = document.createElement('p');
            listItem.innerHTML = 'No comments found';
            list.appendChild(listItem);

        } else {
            //Otherwise we'll loop through the comments array and add them in list fashion. Below each comment we will output the author's name in italics to show it's not part of the comment
            for (let i = 0; i < articleObj.comments.length; i++) {
                let listItem = document.createElement('li');
                listItem.id = `${articleObj.comments[i].id}`;
                listItem.innerHTML = `${articleObj.comments[i].content} <br> <em>${articleObj.comments[i].fullName}</em>`;
                list.appendChild(listItem);
            }
        }
        //To begin with we'll hide the comments and set the height to 0 (so that it doesn't appear on a partial article)
        list.style.visibility = 'hidden';
        list.style.height = '0px';
        articleDivElement.append(list);

        let articlesDiv = document.querySelector("#articles-inner");
        articlesDiv.appendChild(articleDivElement);

        articleDivElement.querySelector('.article-author').addEventListener('click', displayAuthorDetailsOnPage);
        articleDivElement.querySelector('.article-read-more').addEventListener('click', displayFullArticleOnPage);
    }

    async function displayFullArticleOnPage(event) {
        let articleObject = event.target.previousElementSibling.getAttribute("data-article-content");
        let articleContentDiv = event.target.previousElementSibling;
        articleContentDiv.innerText = articleObject;
        let parent = event.target.parentElement;

        //Below we'll remove the custom attribute data-article to an empty string - to save on memory as it's no longer required
        articleContentDiv.removeAttribute("data-article-content");
        let list = event.target.nextElementSibling;
        list.style.visibility = 'visible';
        list.style.height = 'auto';
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