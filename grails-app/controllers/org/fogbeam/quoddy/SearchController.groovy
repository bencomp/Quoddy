package org.fogbeam.quoddy


import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.index.IndexWriter.MaxFieldLength
import org.apache.lucene.queryParser.MultiFieldQueryParser
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.util.Version
import org.fogbeam.quoddy.search.SearchResult


class SearchController 
{

	def siteConfigService;
	def userService;
	def searchService;
	def jmsService;
	
	def index = 
	{	
		[]		
	}
	
	
	def doAdvancedSearch =
	{
		
		// search using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		
		println "doAdvancedSearch, queryString: ${queryString}";
		
		boolean bSearchEverything = false;
		boolean bSearchStatusUpdates = false;
		boolean bSearchCalendarFeedItems = false;
		boolean bSearchBusSubItems = false;
		boolean bSearchActivitiUserTasks = false;
		boolean bSearchRssFeedItems = false;
		boolean bSearchActivityStreamItems = false;		
		boolean bSearchUsers = false;
		boolean bSearchFriends = false;
		
		String searchEverything = params.searchEverything;
		println "searchEverything: ${searchEverything}";
		bSearchEverything = searchEverything ? true: false;
		
		if( bSearchEverything )
		{
			println( "setting all flags to true");
			bSearchStatusUpdates = true;
			bSearchCalendarFeedItems = true;
			bSearchBusSubItems = true;
			bSearchActivitiUserTasks = true;
			bSearchRssFeedItems = true;
			bSearchActivityStreamItems = true;
			bSearchUsers = true;
			bSearchFriends = true;
		}
		else
		{

		
			String searchStatusUpdates = params.searchStatusUpdates;
			bSearchStatusUpdates = searchStatusUpdates ? true: false;
			println "searchStatusUpdates: ${searchStatusUpdates}";
		
			String searchCalendarFeedItems = params.searchCalendarFeedItems;
			bSearchCalendarFeedItems = searchCalendarFeedItems ? true : false;
			println "searchCalendarFeedItems: ${searchCalendarFeedItems}";
		
			String searchBusSubItems = params.searchBusSubItems;
			bSearchBusSubItems = searchBusSubItems ? true : false;
			println "searchBusSubItems: ${searchBusSubItems}";
					
			String searchActivitiUserTasks = params.searchActivitiUserTasks;
			bSearchActivitiUserTasks = searchActivitiUserTasks ? true : false;
			println "searchActivitiUserTasks: ${searchActivitiUserTasks}";
			
			String searchRssFeedItems = params.searchRssFeedItems;
			bSearchRssFeedItems = searchRssFeedItems ? true : false;
			println "searchRssFeedItems: ${searchRssFeedItems}";
		
			String searchActivityStreamItems = params.searchActivityStreamItems;
			bSearchActivityStreamItems = searchActivityStreamItems ? true : false;
			println "searchActivityStreamItems: ${searchActivityStreamItems}";
			
			String searchUsers = params.searchUsers;
			bSearchUsers = searchUsers ? true : false;
			println "searchUsers: ${searchUsers}";
			
			String searchFriends = params.searchFriends;
			bSearchFriends = searchFriends ? true : false;
			println "searchFriends: ${searchFriends}";
		
		}
		
		
		List<SearchResult> searchResults = null;
		
//		if( bSearchEverything )
//		{
//			searchResults = searchService.doEverythingSearch( queryString );
//		}
//		else 
//		{
		
		searchResults = new ArrayList<SearchResult>();	
		if( bSearchStatusUpdates )
		{
			println "searching status updates";
			List<SearchResult> tempResults = searchService.doStatusUpdateSearch( queryString );
			println "SearchStatusUpdates returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchCalendarFeedItems )
		{
			println "searching calendar feed items";
			List<SearchResult> tempResults = searchService.doCalendarFeedItemSearch( queryString );
			println "SearchCalendarFeedItems returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchBusSubItems )
		{
			println "searching business event subscription items";
			List<SearchResult> tempResults = searchService.doBusinessSubscriptionItemSearch( queryString );
			println "SearchBusSubItems returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchActivitiUserTasks )
		{
			println "searching Activiti User Tasks";
			List<SearchResult> tempResults = searchService.doActivitiUserTaskSearch( queryString );
			println "SearchBusSubItems returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
			
		}
		
		if( bSearchRssFeedItems )
		{
			println "searching rss feed items";
			List<SearchResult> tempResults = searchService.doRssFeedItemSearch( queryString );
			println "SearchRssFeedItems returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchActivityStreamItems )
		{
			println "searching activity stream items";
			List<SearchResult> tempResults = searchService.doActivityStreamItemSearch( queryString );
			println "SearchActivityStreamItems returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchUsers )
		{
			println "searching users";
			List<SearchResult> tempResults = searchService.doUserSearch( queryString );
			println "SearchUsers returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();
		}
		
		if( bSearchFriends )
		{
			println "searching friends";
			List<SearchResult> tempResults = searchService.doFriendSearch( queryString, session.user );
			println "SearchFriends returned " + tempResults.size() + " results";
			searchResults.addAll( tempResults );
			println "searchResults.size() = " + searchResults.size();		}

		
		// }
		
		
		println "found some results: ${searchResults.size()}";
		
		render( view:'everythingSearchResults', model:[searchResults:searchResults]);
	}
	
	def showAdvanced =
	{
		[]	
	}
	
	def searchUsers = 
	{
	
		// search users using supplied parameters and return the
		// model for rendering...				
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
		
		List<SearchResult> results = searchService.doUserSearch();
		println "found some users: ${results.size()}";
		
		render( view:'userSearchResults', model:[allUsers:results]);
	}

	def searchIFollow = 
	{
		
	}
	
	def doPeopleSearch =
	{
		
		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching People, queryString: ${queryString}";
				
		
		
		List<SearchResult> results = searchService.doPeopleSearch( queryString );
		println "found some users: ${results.size()}";
		
		render( view:'peopleSearchResults', model:[allUsers:results]);
		
	}
	
	def doIFollowSearch = 
	{
		
		println "Searching IFollow";
		
		User user = session.user;
		

		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching IFollow, queryString: ${queryString}";
				

		List<SearchResult> results = searchService.doIFollowSearch( queryString );
		println "found some users: ${results.size()}";
		
		
		render( view:'iFollowSearchResults', model:[allUsers:results]);
		
			
	}
	
	def searchFriends = {
	
	}

	def searchPeople = {
	
	}
		
	def doFriendSearch = {
		println "Searching Friends";	
		
		User user = session.user;


		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
				
		List<SearchResult> results = searchService.doFriendSearch( queryString );
		
		println "found some users: ${results.size()}";
		
		
		println "done"
		render( view:'friendSearchResults', model:[allUsers:results]);
		
			
	}
	
	
	def rebuildAll = {
	
		// send JMS message requesting ALL index rebuild
		def msg = [ msgType:'REINDEX_ALL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>All Indexes Rebuilding...</h1></body></html>" );
		
	}
	
	def rebuildPersonIndex = {
		
		// TODO: send JMS message requesting PERSON index rebuild
		def msg = [ msgType:'REINDEX_PERSON'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>Person Index Rebuilding...</h1></body></html>" );
		
	}
	
	def rebuildGeneralIndex = {
		
		// TODO: send JMS message requesting GENERAL index rebuild
		def msg = [ msgType:'REINDEX_GENERAL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>General Index Rebuilding...</title></head><body><h1>General Index Rebuilding...</h1></body></html>" );
		
	}
	
	
}
