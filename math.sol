pragma solidity ^0.4.4;

contract Math {
    uint g_counter;
    function add(uint i)
    {
        g_counter+=i;
    }
    function get() constant returns (uint c)
    {
        return g_counter;
    }
}
