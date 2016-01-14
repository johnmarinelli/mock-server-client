require_relative '../lib/mock_server/mock_data'
require 'json'

class MockServerApp
  private
  attr_reader :mock_data

  public
  def initialize
    @mock_data = MockDataGenerator.new
  end

  def call(env)
    req = Rack::Request.new env
    [
      200,
      {
        'Content-Type' => 'application/json',
      },
      [JSON.generate(@mock_data.get_mocks(10))]
    ]
  end
end
