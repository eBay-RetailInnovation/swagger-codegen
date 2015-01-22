public protocol RIModel
{
    init?(dictionary: [String: AnyObject])
    init(forcedDictionary: [String: AnyObject])
    func dictionaryRepresentation() -> [String: AnyObject]
    func parseDictionary(dictionary: [String: AnyObject])
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

func RIBuildModelArray<T:RIModel>(input: [[String:AnyObject]]) -> [T]
{
    return input.ri_filterMap({ (data) in T(dictionary: data) })
}

func RIFlattenOptional<T>(optional: T??) -> T?
{
    switch optional
    {
        case .Some(let value):
            return value
        case .None:
            return nil
    }
}
