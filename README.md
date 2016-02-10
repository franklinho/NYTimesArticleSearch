# Project 2 - New York Times Article Search

New York Times article search is an android app that allows a user to read NY Times articles by searching and filtering. The app utilizes the NYTimes API to display article images and headlines, and opens up the articles in a webview.

Time spent: 9 hours spent in total

## User Stories


  * The following user stories must be completed:
    * :white_check_mark: User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API. (3 points)
    * :white_check_mark: User can click on "settings" which allows selection of advanced search options to filter results. (3 points)
    * :white_check_mark: User can configure advanced search filters such as: (points included above)
      * :white_check_mark: Begin Date (using a date picker)
      * :white_check_mark: News desk values (Arts, Fashion & Style, Sports)
      * :white_check_mark: Sort order (oldest or newest)
    * :white_check_mark: Subsequent searches will have any filters applied to the search results. (1 point)
    * :white_check_mark: User can tap on any article in results to view the contents in an embedded browser. (2 points)
    * :white_check_mark: User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search. (1 point)
  * The following advanced user stories are optional but recommended:
    * :white_large_square: Advanced: Robust error handling, check if internet is available, handle error cases, network failures. (1 point)
    * :white_check_mark: Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText. (1 point)
    * :white_check_mark: Advanced: User can share a link to their friends or email it to themselves. (1 point)
    * :white_check_mark: Advanced: Replace Filter Settings Activity with a lightweight modal overlay. (2 points)
    * :white_check_mark: Advanced: Improve the user interface and experiment with image assets and/or styling and coloring (1 to 3 points depending on the difficulty of UI improvements)
    * :white_check_mark: Bonus: Use the RecyclerView with the StaggeredGridLayoutManager to display improve the grid of image results (see Picasso guide too). (2 points)
    * :white_large_square: Bonus: For different news articles that only have text or only have images, use Heterogenous Layouts with RecyclerView. (2 points)
    * :white_check_mark: Bonus: Apply the popular ButterKnife annotation library to reduce view boilerplate. (1 point)
    * :white_check_mark: Bonus: Use Parcelable instead of Serializable using the popular Parceler library. (1 point)
    * :white_large_square: Bonus: Leverage the popular GSON library to streamline the parsing of JSON data. (1 point)
    * :white_check_mark: Bonus: Replace Picasso with Glide for more efficient image rendering. (1 point)


## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<!-- ![General Functionality](https://github.com/franklinho/InstagramHomework/blob/master/InstagramHomeworkWalkthrough.gif) -->


GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

  * Running into issues with images being loaded during infinite scroll. Images will only load if you scroll past a certain distance.
  * Ran into issues with sharing webview url, but eventually figured it out
  * Utilized formatted edit text field with validation rather than datepicker


## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Butterknife](http://jakewharton.github.io/butterknife/) - Removes boilerplate code by binding view IDs to objects
- [Parceler](https://github.com/johncarl81/parceler) - Simplifies parceling objects
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library
- [GSON](https://github.com/google/gson) - Java serialization library for making JSON parsing easier


## License

    Copyright 2016 Franklin Ho

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.