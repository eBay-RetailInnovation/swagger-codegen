import Foundation

protocol _RIModelDictionaryInit: class
{
    init?(dictionary: [String: AnyObject])
}

public class RIModel: _RIModelDictionaryInit
{
    init()
    {
    }
    
    public required init?(dictionary: [String: AnyObject])
    {
    }
    
    public func dictionaryRepresentation() -> [String: AnyObject]
    {
        return [:]
    }
    
    public func parseDictionary(dictionary: [String: AnyObject])
    {
    }
}

public enum RIAPIServer: String
{
    case Production = "https://api2.batcavelabs.com"
    case Localhost = "http://localhost:8080"
    
    public var URL: NSURL?
    {
        return NSURL(string: self.rawValue)
    }
    
    public func URLForPath(path: String) -> NSURL?
    {
        return NSURL(string: self.rawValue).ri_flatMap({ (URL) in NSURL(string: path, relativeToURL: URL) })
    }
}

extension Optional
{
    func ri_flatMap<U>(f: T -> U?) -> U?
    {
        switch self
        {
            case .Some(let value):
                return f(value)
            case .None:
                return nil
        }
    }
}

extension Array
{
    func ri_filterMap<U>(f: T -> U?) -> [U]
    {
        var output: [U] = []
        
        for item in self
        {
            if let mapped = f(item)
            {
                output.append(mapped)
            }
        }
        
        return output
    }
}

func RIBuildModelArray<T:_RIModelDictionaryInit>(input: [[String:AnyObject]]) -> [T]
{
    return input.ri_filterMap({ (data) in T(dictionary: data) })
}

func RICreateError(message: String, URL: NSURL, code: Int) -> NSError
{
    let dictionary: [NSObject:AnyObject] = [
        NSLocalizedDescriptionKey: message,
        NSURLErrorKey: URL
    ]
    
    return NSError(domain: "com.batcavelabs", code: code, userInfo: dictionary)
}

func RIParsePrimitive(input: AnyObject) -> CFAbsoluteTime?
{
    return 0
}

func RIParsePrimitive(input: AnyObject) -> String?
{
    return input as? String
}

func RIParsePrimitive(input: AnyObject) -> Int?
{
    return (input as? NSNumber)?.integerValue
}

func RIParsePrimitive(input: AnyObject) -> Float?
{
    return (input as? NSNumber)?.floatValue
}

func RIParsePrimitive(input: AnyObject) -> Bool?
{
    return (input as? NSNumber)?.boolValue
}
