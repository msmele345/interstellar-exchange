# interstellar-exchange 
## A magical electronic stock trading exchange that consumes a live quote data feed and executes trades bases off bid/ask spreads. 
 
## Realtime quote feed flow:



### QuoteListener:  
    - Consumes Json Messages with a quote payload from an ActiveMQ queue.
    - Converts ActiveMqTextMessages to Bid and Ask POJOs
    - Message source is the livequotes feed (see livequotes app)
    
    
### RealTimeOrchestrator:
    - Groups all inbound quotes into trade groups for symbol
    
    
### RealTimeMatcherService 
    - Takes trade groups for different symbols with different bids and offers 
    - Each group has a symbol and a list of prices - may or may not be trade eligible
    - Processes and passes list of bids/offers for a symbol and sends to Trade Matcher Service.
    
    
### TradeMatcherService 
    - Uses MarketCheckHelper and TradeExecution helper to identify
        which bid/ask quotes are trade eligible. Executes eligible trades
    - Saves any trades made to trade repo
    - Updates bid/ask repos by deleting any bid/asks used in trade to prevent dupe trades


### MISC
    - Algorithms determine if bid/ask spread for quote is narrow enough to execute a trade realtime.
    - A seperate service runs scheduled based processing
        - Involves searching through repositories for unused bid and asks that could be trade eligable and executes a trade if so.
    - Eligible trades are executed at the midpoint (mid point between bid and ask.
